(ns ducks.endpoint.users-test
  (:require [com.stuartsierra.component :as component]
            [clojure.test :refer :all]
            [kerodon.core :refer :all]
            [kerodon.test :refer :all]
            [ducks.endpoint.users :as users]))

;; (def handler
;;   (users/users-endpoint {}))

;; (deftest smoke-test
;;   (testing "index page exists"
;;     (-> (session handler)
;;         (visit "/")
;;         (has (status? 200) "page exists"))))
