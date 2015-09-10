(ns ducks.system
  (:require [clojure.java.io :as io]
            [com.stuartsierra.component :as component]
            [duct.component.endpoint :refer [endpoint-component]]
            [duct.component.handler :refer [handler-component]]
            [duct.component.hikaricp :refer [hikaricp]]
            [duct.component.ragtime :refer [ragtime]]
            [duct.middleware.not-found :refer [wrap-not-found]]
            [meta-merge.core :refer [meta-merge]]
            [ring.component.jetty :refer [jetty-server]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults site-defaults]]
            [ring.middleware.webjars :refer [wrap-webjars]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ducks.endpoint.users :refer [users-endpoint]]
            [ducks.endpoint.auth :refer [auth-endpoint]]
            [compojure.core :refer [context]]))

(def base-config
  {:app {:middleware [[wrap-not-found :not-found]
                      [wrap-json-body {:keywords? true :bigdecimals? true}]
                      [wrap-json-response]
                      [wrap-webjars]
                      [wrap-defaults :defaults]]
         :not-found  (io/resource "ducks/errors/404.html")
         :defaults   (meta-merge api-defaults {:static {:resources "ducks/public"}})}
   :ragtime {:resource-path "ducks/migrations"}})

(defn new-system [config]
  (let [config (meta-merge base-config config)]
    (-> (component/system-map
         :app  (handler-component (:app config))
         :http (jetty-server (:http config))
         :db   (hikaricp (:db config))
         :ragtime (ragtime (:ragtime config))
         :users (endpoint-component #(context "/users" [] (users-endpoint %)))
         :auth (endpoint-component #(context "/auth" [] (auth-endpoint %))))
        (component/system-using
         {:http [:app]
          :app [:users :auth]
          :ragtime [:db]
          :users [:db]
          :auth [:db]}))))
