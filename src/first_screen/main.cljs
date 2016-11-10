
(ns first-screen.main
  (:require [respo.core :refer [render! clear-cache! falsify-stage! render-element]]
            [first-screen.comp.container :refer [comp-container]]
            [cljs.reader :refer [read-string]]))

(defonce store-ref (atom {:loading {:content? false}}))

(defn dispatch! [op op-data]
  (println "dispatch!" op op-data)
  (let [store (case op :loaded (assoc-in @store-ref [:loading :content?] true) @store-ref)]
    (reset! store-ref store)))

(defonce states-ref (atom {}))

(defn render-app! []
  (let [target (.querySelector js/document "#app")]
    (println "store:" @store-ref)
    (render!
     (comp-container @store-ref #{:dynamic :shell} true)
     target
     dispatch!
     states-ref)))

(def ssr-stages
  (let [ssr-element (.querySelector js/document "#ssr-stages")
        ssr-markup (.getAttribute ssr-element "content")]
    (read-string ssr-markup)))

(defn -main! []
  (enable-console-print!)
  (if (not (empty? ssr-stages))
    (let [target (.querySelector js/document "#app")]
      (falsify-stage!
       target
       (render-element (comp-container @store-ref ssr-stages false) states-ref)
       dispatch!)))
  (js/setTimeout (fn [] (render-app!)) 1000)
  (add-watch store-ref :changes render-app!)
  (add-watch states-ref :changes render-app!)
  (println "app started!")
  (js/setTimeout (fn [] (dispatch! :loaded nil)) 2000))

(defn on-jsload! [] (clear-cache!) (render-app!) (println "code update."))

(set! (.-onload js/window) -main!)
