(defproject adt "zjhmale/ADT 0.1.0"
  :description "ADT support for Clojure"
  :url "https://github.com/zjhmale/ADT"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [potemkin "0.4.3"]
                 [acolfut "0.3.3"]]
  :plugins [[lein-colortest "0.3.0"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
