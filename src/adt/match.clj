(ns adt.match
  (require [adt.utils :refer :all]))

(defn- symbol-tests-and-bindings
  [template vsymbol]
  [`(= (quote ~(resolve-symbol template)) ~vsymbol)
   []])

(defn- sequential-tests-and-bindings
  [template vsymbol]
  (let [enum-values (map list template (range (count template)))
        ; Non-symbols in the template create an equality test with the
        ; corresponding value in the object's value list
        tests       (map (fn [[v i]] `(= ~v (nth ~vsymbol ~i)))
                         (filter (complement #(symbol? (first %))) enum-values))
        ; Symbols in the template become bindings to the corresponding
        ; value in the object. However, if a symbol occurs more than once,
        ; only one binding is generated, and equality tests are added
        ; for the other values.
        bindings    (reduce (fn [map [symbol index]]
                              (assoc map symbol
                                         (conj (get map symbol []) index)))
                            {}
                            (filter #(symbol? (first %)) enum-values))
        tests       (concat tests
                            (map (fn [[symbol indices]]
                                   (cons `= (map #(list `nth vsymbol %) indices)))
                                 (filter #(> (count (second %)) 1) bindings)))
        bindings    (mapcat (fn [[symbol indices]]
                              [symbol (list `nth vsymbol (first indices))])
                            bindings)]
    [tests (vec bindings)]))

(defn- constr-tests-and-bindings
  [template cfsymbol]
  (let [[tag & values] template
        cfasymbol (gensym)
        [tests bindings] (sequential-tests-and-bindings values cfasymbol)
        argtests  (if (empty? tests)
                    tests
                    `((let [~cfasymbol (rest ~cfsymbol)] ~@tests)))]
    [`(and (seq? ~cfsymbol)
           (= (quote ~(resolve-symbol tag)) (first ~cfsymbol))
           ~@argtests)
     `[~cfasymbol (rest ~cfsymbol) ~@bindings]]))

(defn- list-tests-and-bindings
  [template vsymbol]
  (let [[tests bindings] (sequential-tests-and-bindings template vsymbol)]
    [`(and (list? ~vsymbol) ~@tests)
     bindings]))

(defn- vector-tests-and-bindings
  [template vsymbol]
  (let [[tests bindings] (sequential-tests-and-bindings template vsymbol)]
    [`(and (vector? ~vsymbol) ~@tests)
     bindings]))

(defn- map-tests-and-bindings
  [template vsymbol]
  (let [; First test if the given keys are all present.
        tests    (map (fn [[k v]] `(contains? ~vsymbol ~k)) template)
        ; Non-symbols in the template create an equality test with the
        ; corresponding value in the object's value list.
        tests    (concat tests
                         (map (fn [[k v]] `(= ~v (~k ~vsymbol)))
                              (filter (complement #(symbol? (second %))) template)))
        ; Symbols in the template become bindings to the corresponding
        ; value in the object. However, if a symbol occurs more than once,
        ; only one binding is generated, and equality tests are added
        ; for the other values.
        bindings (reduce (fn [map [key symbol]]
                           (assoc map symbol
                                      (conj (get map symbol []) key)))
                         {}
                         (filter #(symbol? (second %)) template))
        tests    (concat tests
                         (map (fn [[symbol keys]]
                                (cons `= (map #(list % vsymbol) keys)))
                              (filter #(> (count (second %)) 1) bindings)))
        bindings (mapcat (fn [[symbol keys]]
                           [symbol (list (first keys) vsymbol)])
                         bindings)]
    [`(and (map? ~vsymbol) ~@tests)
     (vec bindings)]))

(defn- tests-and-bindings
  [template vsymbol cfsymbol]
  (cond (symbol? template)
        (symbol-tests-and-bindings template cfsymbol)
        (seq? template)
        (if (= (first template) 'quote)
          (list-tests-and-bindings (second template) vsymbol)
          (constr-tests-and-bindings template cfsymbol))
        (vector? template)
        (vector-tests-and-bindings template vsymbol)
        (map? template)
        (map-tests-and-bindings template vsymbol)
        :else
        (throw (IllegalArgumentException. "illegal template for match"))))

(defmacro match
  "Given a value and a list of template-expr clauses, evaluate the first
   expr whose template matches the value. There are four kinds of templates:
   1) Lists of the form (tag x1 x2 ...) match instances of types
      whose constructor has the same form as the list.
   2) Quoted lists of the form '(x1 x2 ...) match lists of the same
      length.
   3) Vectors of the form [x1 x2 ...] match vectors of the same length.
   4) Maps of the form {:key1 x1 :key2 x2 ...} match maps that have
      the same keys as the template, but which can have additional keys
      that are not part of the template.
   The values x1, x2, ... can be symbols or non-symbol values. Non-symbols
   must be equal to the corresponding values in the object to be matched.
   Symbols will be bound to the corresponding value in the object in the
   evaluation of expr. If the same symbol occurs more than once in a,
   template the corresponding elements of the object must be equal
   for the template to match."
  [value & clauses]
  (when (odd? (count clauses))
    (throw (Exception. "Odd number of elements in match expression")))
  (let [vsymbol  (gensym)
        cfsymbol (gensym)
        terms    (mapcat (fn [[template expr]]
                           (if (= template :else)
                             [template expr]
                             (let [[tests bindings]
                                   (tests-and-bindings template vsymbol cfsymbol)]
                               [tests
                                (if (empty? bindings)
                                  expr
                                  `(let ~bindings ~expr))])))
                         (partition 2 clauses))]
    `(let [~vsymbol ~value
           ~cfsymbol (constructor-form ~vsymbol)]
       (cond ~@terms))))
