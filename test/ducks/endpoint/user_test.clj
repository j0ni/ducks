(ns ducks.endpoint.user-test
  (:require [com.stuartsierra.component :as component]
            [clojure.test :refer :all]
            [kerodon.core :refer :all]
            [kerodon.test :refer :all]
            [ducks.endpoint.user :as user]))

(def handler
  (user/user-endpoint {}))

(deftest smoke-test
  (testing "index page exists"
    (-> (session handler)
        (visit "/")
        (has (status? 200) "page exists"))))
