
(ns ssr-stages.boot
  (:require
    [respo.alias :refer [html head title script style meta' div link body]]
    [respo.render.html :refer [make-html make-string]]
    [first-screen.comp.container :refer [comp-container]]))

(defn use-text [x] {:attrs {:innerHTML x}})
(defn html-dsl [data html-content ssr-stages]
  (make-html
    (html {}
      (head {}
        (title (use-text (str "First Screen")))
        (link {:attrs {:rel "icon" :type "image/png" :href "http://logo.respo.site/respo.png"}})
        (meta' {:attrs {:charset "utf-8"}})
        (meta' {:attrs {:name "viewport" :content "width=device-width, initial-scale=1"}})
        (meta' {:attrs {:id "ssr-stages" :content (pr-str ssr-stages)}})
        (style (use-text "body {margin: 0;}"))
        (style (use-text "body * {box-sizing: border-box;}"))
        (style (use-text (.readFileSync (js/require "fs") "./css/rotate.css")))
        (script {:attrs {:id "config" :type "text/edn" :innerHTML (pr-str data)}}))
      (body {}
        (div {:attrs {:id "app" :innerHTML html-content}})
        (script {:attrs {:src "main.js"}})))))

(defn generate-html [ssr-stages]
  (let [ tree (comp-container {} ssr-stages false)
         html-content (make-string tree)]
    (html-dsl {:build? true} html-content ssr-stages)))

(defn spit [file-name content]
  (let [fs (js/require "fs")]
    (.writeFileSync fs file-name content)))

(defn -main []
  (spit "target/index.html" (generate-html #{:shell})))

(-main)
