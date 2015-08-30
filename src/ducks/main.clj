(ns ducks.main
  (:gen-class)
  (:require [clojure.java.io :as io]
            [com.stuartsierra.component :as component]
            [duct.middleware.errors :refer [wrap-hide-errors]]
            [meta-merge.core :refer [meta-merge]]
            [ducks.config :as config]
            [ducks.system :refer [new-system]]
            [taoensso.timbre :as timbre :refer [info infof]]
            [duct.component.ragtime :as ragtime]))

(def prod-config
  {:app {:middleware     [[wrap-hide-errors :internal-error]]
         :internal-error (io/resource "errors/500.html")}})

(def config
  (meta-merge config/defaults
              config/environ
              prod-config))

(defn -main [& args]
  (info "Booting ducks...")
  (let [system (new-system config)]
    (infof "Starting HTTP server on port %d" (-> system :http :port))
    (try
      (-> (component/start system)
          ;; Would be nice for this to be done before bringing the
          ;; HTTP component online.
          (:ragtime)
          (ragtime/migrate))
      (catch Exception e
        (info e)))))
