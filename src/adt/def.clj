(ns adt.def
  (require [adt.utils :refer :all]))

(defn- constructor-code
  [meta-map-symbol constructor]
  (if (symbol? constructor)
    `(def ~constructor
       (with-meta {::tag (quote ~(qualified-symbol constructor))}
                  ~meta-map-symbol))
    (let [[name & args] constructor
          keys (cons ::tag (map (comp keyword str) args))]
      (if (empty? args)
        (throw (IllegalArgumentException. "zero argument constructor"))
        `(let [~'basis (create-struct ~@keys)]
           (defn ~name ~(vec args)
             (with-meta (struct ~'basis (quote ~(qualified-symbol name)) ~@args)
                        ~meta-map-symbol)))))))

(defmacro defadt
  "Define an algebraic data type name by an exhaustive list of constructors.
   Each constructor can be a symbol (argument-free constructor) or a
   list consisting of a tag symbol followed by the argument symbols.
   The data type tag must be a keyword."
  [type-tag & constructors]
  (let [meta-map-symbol (gensym "mm")]
    `(let [~meta-map-symbol {:type ~type-tag}]
       (derive ~type-tag ::adt)
       ~@(map (partial constructor-code meta-map-symbol) constructors))))
