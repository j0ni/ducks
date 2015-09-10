(ns ducks.endpoint.users
  (:require [compojure.core :refer (GET POST PUT DELETE routes)]
            [ring.util.response :as response]
            [taoensso.timbre :refer (info infof)]
            [clojure.java.jdbc :as jdbc]
            [clj-uuid :as uuid]
            [ring.util.http-response :as http-response]
            [ducks.model.user :as user]))

(defn find-users [db req]
  (response/response (user/list-users db)))

(defn find-one-user [db id]
  (if-let [user (user/find-user db id)]
    (response/response user)
    (http-response/not-found id)))

(defn create-user [db username first-name last-name]
  (let [id (user/create-user db {:username username
                                 :first-name first-name
                                 :last-name last-name})]
    (response/response {:id id})))

(defn update-user [db id username first-name last-name]
  (if-let [user (user/update-user db {:id id
                                      :username username
                                      :first-name first-name
                                      :last-name last-name})]
    (response/response user)
    (http-response/not-found id)))

(defn delete-user [db id]
  (if (-> (user/delete-user db id)
          (first)
          (pos?))
    (http-response/no-content)
    (http-response/not-found id)))

(defn users-endpoint [{{db :spec} :db}]
  (routes
   (GET "/" req
     (find-users db req))
   
   (GET "/:id" [id]
     (find-one-user db id))
   
   (POST "/" {:keys [body]}
     (let [{:strs [username first-name last-name]} body]
       (create-user db username first-name last-name)))
   
   (PUT "/:id" [id :as {body :body}]
     (let [{:strs [username first-name last-name]} body]
       (update-user db id username first-name last-name)))
   
   (DELETE "/:id" [id]
     (delete-user db id))))

