(ns ducks.endpoint.auth
  (:require [compojure.core :refer (POST routes)]
            [ring.util.http-response :as response]
            [crypto.password.bcrypt :as password]
            [ducks.model.user :as user]
            [clj-uuid :as uuid]))

;; {
;;   :authenticated-user Str
;;   :ducks-session { }
;; }

(defn authenticate [db username password]
  (if-let [user (user/find-user-by-username-with-password db username)]
    (if (and (some? (:password user))
             (password/check password (:password user)))
      (merge (response/ok)
             {:session {:authenticated-user username
                        :ducks-session {}}})
      (response/unauthorized))
    (response/unauthorized)))

(defn logout [req]
  (response/ok (dissoc req :session)))

(defn auth-endpoint [{{db :spec} :db}]
  (routes
   (POST "/login" {:keys [body]}
     (let [{:strs [username password]} body]
       (authenticate db username password)))

   (POST "/logout" req
     (logout req))))
