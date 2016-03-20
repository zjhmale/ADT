(ns adt.utils)

(defn qualified-symbol
  [s]
  (symbol (str *ns*) (str s)))

(defn resolve-symbol
  [s]
  (if-let [var (resolve s)]
    (symbol (str (.ns var)) (str (.sym var)))
    s))

(defn constructor-form
  [o]
  (let [v (vals o)]
    (if (= 1 (count v))
      (first v)
      v)))
