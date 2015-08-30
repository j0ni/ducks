(ns ducks.endpoint.users
  (:require [compojure.core :refer (GET POST PUT DELETE routes)]
            [ring.util.response :as response]
            [taoensso.timbre :refer (info)]))

(defn find-users [req]
  (response/response []))

(defn find-one-user [id]
  (response/response {:id id}))

(defn create-user [first-name last-name]
  (response/response {:reality-check :nope
                      :first-name first-name
                      :last-name last-name}))

(defn update-user [id first-name last-name]
  (response/response {:reality-check :nope
                      :id id
                      :first-name first-name
                      :last-name last-name}))

(defn delete-user [id]
  (response/response {:reality-check :nope :id id}))

(defn users-endpoint [{{db :spec} :db}]
  (routes
   (GET "/" req
     (find-users req))
   
   (GET "/:id" [id]
     (find-one-user id))
   
   (POST "/" [first-name last-name]
     (create-user first-name last-name))
   
   (PUT "/:id" [id first-name last-name]
     (update-user id first-name last-name))
   
   (DELETE "/:id" [id]
     (delete-user id))))

