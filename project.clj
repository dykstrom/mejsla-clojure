(defproject mejsla-clojure "0.1.0-SNAPSHOT"
  :description "Code from Mejsla's Clojure Study-group"
  :url "https://github.com/dykstrom/mejsla-clojure"
  :license {:name "GPLv3"
            :url "https://www.gnu.org/licenses/gpl-3.0.en.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ysera "1.0.0"]
                 [digest "1.4.5"]
                 [org.clojure/data.priority-map "0.0.7"]
                 [thunknyc/profile "0.5.2"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
