(ns ducks.model.user
  (:require [crypto.password.bcrypt :as password]
            [clojure.java.jdbc :as jdbc]
            [clj-uuid :as uuid]))

(defn list-users
  [db]
  (jdbc/query db ["select * from users"]))

(defn find-user
  [db id]
  (let [users (jdbc/query db ["select * from users where id = ?" (uuid/as-uuid id)])]
    (first users)))


(defn find-user-by-username-with-password
  [db username]
  (let [users (jdbc/query db ["select * from users where username = ?" username])]
    (first users)))

(defn find-user-by-username
  [db username]
  (dissoc (find-user-by-username-with-password db username) :password))

(defn create-user
  [db {:keys [username first-name last-name password]}]
  (let [id (uuid/v4)
        hashed_password (password/encrypt password)]
    (jdbc/insert! db :users
      {:id id
       :username username
       :first_name first-name
       :last_name last-name
       :password hashed_password})
    id))

(defn update-user
  "Nil means not found"
  [db {:keys [id username first-name last-name]}]
  (jdbc/update! db :users
    {:username username
     :first_name first-name
     :last_name last-name}
    ["id = ?" (uuid/as-uuid id)]))

(defn delete-user
  [db {:keys [id]}]
  (jdbc/delete! db :users
    ["id = ?" (uuid/as-uuid id)]))


(defn find-one-by-username [username]
  nil)
