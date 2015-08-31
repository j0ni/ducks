(ns ducks.endpoint.users
  (:require [compojure.core :refer (GET POST PUT DELETE routes)]
            [ring.util.response :as response]
            [taoensso.timbre :refer (info)]
            [clojure.java.jdbc :as jdbc]
            [clj-uuid :as uuid]
            [ring.util.http-response :as http-response]))

;; data access layer
;; TODO move this all to its own ns, or a component

(defn db-list-users
  [db]
  (jdbc/query db ["select * from users"]))

(defn db-find-user
  [db id]
  (let [users (jdbc/query db ["select * from users where id = ?" (uuid/as-uuid id)])]
    (first users)))

(defn db-create-user
  [db {:keys [username first-name last-name]}]
  (let [id (uuid/v4)]
    (jdbc/insert! db :users
      {:id id
       :username username
       :first_name first-name
       :last_name last-name})
    id))

(defn db-update-user
  "Nil means not found"
  [db {:keys [id username first-name last-name]}]
  (jdbc/update! db :users
    {:username username
     :first_name first-name
     :last_name last-name}
    ["id = ?" (uuid/as-uuid id)]))

(defn db-delete-user
  [db {:keys [id]}]
  (jdbc/delete! db :users
    ["id = ?" (uuid/as-uuid id)]))

;; end of data access layer

(defn find-users [db req]
  (response/response (db-list-users db)))

(defn find-one-user [db id]
  (if-let [user (db-find-user db id)]
    (response/response user)
    (http-response/not-found id)))

(defn create-user [db username first-name last-name]
  (let [id (db-create-user db {:username username
                               :first-name first-name
                               :last-name last-name})]
    (response/response {:id id})))

(defn update-user [db id username first-name last-name]
  (if-let [user (db-update-user db {:id id
                                    :username username
                                    :first-name first-name
                                    :last-name last-name})]
    (response/response user)
    (http-response/not-found id)))

(defn delete-user [db id]
  (if (-> (db-delete-user db id)
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
   
   (POST "/" [username first-name last-name]
     (create-user db username first-name last-name))
   
   (PUT "/:id" [id username first-name last-name]
     (update-user db id username first-name last-name))
   
   (DELETE "/:id" [id]
     (delete-user db id))))

