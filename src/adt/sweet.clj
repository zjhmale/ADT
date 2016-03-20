(ns adt.sweet
  (require adt.def
           adt.match
           [potemkin :refer [import-vars]]))

(import-vars
  [adt.def defadt]
  [adt.match match])
