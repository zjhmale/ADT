(defproject adt "0.1.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [potemkin "0.4.3"]
                 [acolfut "0.3.3"]]
  :plugins [[lein-colortest "0.3.0"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
