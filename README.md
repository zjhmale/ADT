# adt

Algebraic Data Type

[![Build Status](https://travis-ci.org/zjhmale/ADT.svg?branch=master)](https://travis-ci.org/zjhmale/ADT)

## Installation

[![Clojars Project](http://clojars.org/zjhmale/adt/latest-version.svg)](http://clojars.org/zjhmale/adt)

## Usage

```clojure
:dependencies [[zjhmale/adt "0.1.0"]]
```

## Examples

```clojure
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
```

## License

Copyright © 2010 Rich Hickey and the various contributors

Distributed under the Eclipse Public License, the same as Clojure.
