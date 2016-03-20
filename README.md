# ADT

Algebraic Data Type

[![Build Status](https://travis-ci.org/zjhmale/ADT.svg?branch=master)](https://travis-ci.org/zjhmale/ADT)

## Installation

[![Clojars Project](https://clojars.org/zjhmale/adt/latest-version.svg)](https://clojars.org/zjhmale/adt)

## Usage

```clojure
:dependencies [[zjhmale/adt "0.1.0"]]
```

## Examples

```clojure
(require [adt.sweet :refer :all])

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
```

## License

Copyright © 2010 Rich Hickey and the various contributors

Distributed under the Eclipse Public License, the same as Clojure.
