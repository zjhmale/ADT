(ns adt.sweet-test
  (:require [acolfut.sweet :refer :all]
            [adt.sweet :refer :all]))

(defadt ::tree
  Empty-tree
  (Leaf value)
  (Node left-tree value right-tree))

(defn depth
  [t]
  (match t
    Empty-tree 0
    (Leaf _) 1
    (Node l v r) (inc (max (depth l) (depth r)))))

(defn stringfy
  [t]
  (match t
    Empty-tree "nil"
    (Leaf v) v
    (Node l v r) (format "((%s) %s (%s))"
                         (stringfy l)
                         v
                         (stringfy r))))

(deftest tree-test
  (testing "binary tree"
    (let [b-tree (Node (Leaf :a)
                       :a
                       (Node (Leaf :b)
                             :a
                             (Leaf :c)))]
      (is= (depth Empty-tree) 0)
      (is= (depth (Leaf 33)) 1)
      (is= (depth b-tree) 3)
      (is= (stringfy b-tree)
           "((:a) :a (((:b) :a (:c))))"))))
