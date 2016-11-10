
(ns first-screen.comp.container
  (:require [hsl.core :refer [hsl]]
            [respo-ui.style :as ui]
            [respo-ui.style.colors :as colors]
            [respo.alias :refer [create-comp div span]]
            [respo.comp.space :refer [comp-space]]
            [respo.comp.text :refer [comp-text]]))

(def style-header
  {:align-items :center,
   :background-color colors/motif-dark,
   :padding 16,
   :transition-duration "360ms",
   :height 240})

(def style-entry {:min-width 120, :color :white, :font-size 14, :padding "0 16px"})

(defn render-entry [title] (div {:style style-entry} (comp-text title nil)))

(def style-logo
  {:background-color (hsl 0 0 100), :width 160, :transition-duration "360ms", :height "160"})

(def style-welcome {:color :white, :font-size 40})

(defn render-header [loaded?]
  (div
   {:style (merge
            ui/row
            style-header
            (if loaded? {:background-color colors/motif, :height 60}))}
   (div {:style (merge style-logo (if loaded? {:width 40, :height "40"}))})
   (comp-space 16 nil)
   (if loaded?
     (div {:style ui/row} (render-entry "Home") (render-entry "Data list"))
     (div {:style style-welcome} (comp-text "Wellcome visiting!")))))

(def style-shop
  {:background-color colors/paper,
   :width 200,
   :padding 8,
   :border (str "1px solid " colors/intersected-light),
   :height 64,
   :margin 8})

(def style-body {:transition-duration "360ms", :height 120})

(def style-avatar {:background-color colors/verdant, :width 40, :height 40})

(def style-content {:flex-wrap "wrap", :padding 16})

(defn render-content [loaded?]
  (if loaded?
    (div
     {:style (merge ui/row style-content)}
     (->> (range 1 10)
          (map-indexed
           (fn [idx x]
             [idx
              (div
               {:style (merge ui/row style-shop)}
               (div {:style style-avatar})
               (comp-space 8 nil)
               (comp-text (str "Demo " idx " " x) nil))]))))
    (div
     {:style (merge ui/row style-content)}
     (->> (range 1 5)
          (map-indexed (fn [idx x] [idx (div {:style (merge ui/row style-shop)})]))))))

(def style-loading
  {:animation "rotate 2.4s linear infinite normal",
   :background-color colors/attractive,
   :width 80,
   :transition-duration "360ms",
   :height 80})

(defn render-body [loaded? browser?]
  (div
   {:style (merge ui/column-center style-body (if loaded? {:height 0}))}
   (if (not loaded?)
     (div
      {:style ui/row-center}
      (div {:style (merge style-loading (if browser? {:width 40, :height 40}))})
      (comp-space 40 nil)
      (comp-text (if browser? "Fetching data, showing soon!" "Fetching assets...") nil)))))

(defn render [store ssr-stages browser?]
  (fn [state mutate!]
    (let [loaded? (get-in store [:loading :content?])]
      (div
       {:style (merge ui/global)}
       (render-header loaded?)
       (render-body loaded? browser?)
       (render-content loaded?)))))

(def comp-container (create-comp :container render))
