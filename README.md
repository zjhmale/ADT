# ADT

[![Build Status](https://travis-ci.org/zjhmale/ADT.svg?branch=master)](https://travis-ci.org/zjhmale/ADT)

## Installation

Download from http://example.com/FIXME.

## Usage

FIXME: explanation

    $ java -jar adt-0.1.0-standalone.jar [args]

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

Copyright © 2016 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
