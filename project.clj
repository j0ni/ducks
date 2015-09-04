(defproject ducks "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [;; this all comes from Duct
                 [org.clojure/clojure "1.7.0"]
                 [com.stuartsierra/component "0.2.3"]
                 [compojure "1.4.0"]
                 [duct "0.3.0"]
                 [environ "1.0.0"]
                 [hanami "0.1.0"]
                 [meta-merge "0.1.1"]
                 [ring "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [ring-jetty-component "0.2.2"]
                 [ring-webjars "0.1.1"]
                 [org.slf4j/slf4j-nop "1.7.12"]
                 [org.webjars/normalize.css "3.0.2"]
                 [duct/hikaricp-component "0.1.0"]
                 [org.postgresql/postgresql "9.4-1201-jdbc4"]
                 [duct/ragtime-component "0.1.2"]
                 ;; that's the end of Duct's bits
                 [com.taoensso/timbre "4.1.1"]
                 [ring/ring-json "0.4.0"]
                 [org.clojure/java.jdbc "0.4.1"]
                 [danlentz/clj-uuid "0.1.6"]
                 [metosin/ring-http-response "0.6.4"]
                 [crypto-password "0.1.3"]]
  :plugins [[lein-environ "1.0.0"]
            [lein-gen "0.2.2"]]
  :generators [[duct/generators "0.3.0"]]
  :duct {:ns-prefix ducks}
  :main ^:skip-aot ducks.main
  :uberjar-name "ducks-standalone.jar"
  :target-path "target/%s/"
  :aliases {"gen"   ["generate"]
            "setup" ["do" ["generate" "locals"]]
            "deploy" ["do"
                      ["vcs" "assert-committed"]
                      ["vcs" "push" "heroku" "master"]]}
  :profiles
  {:dev  [:project/dev  :profiles/dev]
   :test [:project/test :profiles/test]
   :uberjar {:aot :all}
   :profiles/dev  {}
   :profiles/test {}
   :project/dev   {:source-paths ["dev"]
                   :repl-options {:init-ns user}
                   :dependencies [[reloaded.repl "0.1.0"]
                                  [org.clojure/tools.namespace "0.2.11"]
                                  [kerodon "0.6.1"]]
                   :env {:port 3000}}
   :project/test  {}})
