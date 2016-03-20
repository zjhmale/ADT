(ns adt.sweet-test
  (:require [acolfut.sweet :refer :all]
            [adt.sweet :refer :all]))

(defadt ::tree
  empty-tree
  (leaf value)
  (node left-tree right-tree))

(defn depth
  [t]
  (match t
    empty-tree 0
    (leaf _) 1
    (node l r) (inc (max (depth l) (depth r)))))

(deftest tree-test
  (testing "binary tree"
    (let [b-tree (node (leaf :a)
                       (node (leaf :b)
                             (leaf :c)))]
      (is= (depth empty-tree) 0)
      (is= (depth (leaf 33)) 1)
      (is= (depth b-tree) 3))))
