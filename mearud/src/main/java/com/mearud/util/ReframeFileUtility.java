package com.mearud.util;

import java.util.HashMap;
import java.util.Map;

public class ReframeFileUtility {

    //public static final Map<String, String> packageContents = {};
    /* FILE TEMPLATES REFRAME */
    private static final String DEPS_CONTENTS = "{:deps {org.clojure/clojure {:mvn/version \"1.10.0\"}\n" +
            "        org.clojure/clojurescript {:mvn/version \"1.10.339\"}\n" +
            "        com.bhauman/figwheel-main {:mvn/version \"0.2.3\"}\n" +
            "        ;; optional but recommended\n" +
            "        com.bhauman/rebel-readline-cljs {:mvn/version \"0.1.4\"}\n" +
            "        reagent {:mvn/version \"0.8.1\"}\n" +
            "        re-frame {:mvn/version \"0.10.6\"}\n" +
            "        cljs-ajax {:mvn/version \"0.8.0\"}\n" +
            "        day8.re-frame/http-fx {:mvn/version \"0.1.6\"}\n" +
            "        clj-commons/secretary {:mvn/version \"1.2.4\"}\n" +
            "        funcool/struct {:mvn/version \"1.3.0\"}}"+
            "\n" +
            " :aliases { :dev {:main-opts [\"--main\" \"cljs.main\" \"--compile-opts\" \"compile-dev.edn\"  \"--compile\" \"APP_NAME.core\" \"--repl\"]}\n" +
            "            :prod {:main-opts [\"--main\" \"cljs.main\" \"--compile-opts\" \"compile-prod.edn\" \"--compile\" \"APP_NAME.core\" \"--serve\"]}}\n" +
            "\n" +
            " :paths [\"src\" \"resources\" \"target\"]}";

    private static final String REPL_OPTS = "^{}\n" +
            "{\n" +
            " :analyze-path \"src/\"\n" +
            " :repl-requires ['[cljs.repl :refer-macros [source doc find-doc apropos dir pst]]\n" +
            "                 '[cljs.pprint :refer [pprint] :refer-macros [pp]]]\n" +
            " :repl-verbose true\n" +
            " :working-dir \"work/transpiled/\"\n" +
            " :watch \"src/\"\n" +
            " :watch-fn (fn [] (println \"REPL...\"))\n" +
            " :static-dir [\"resources/public/\" \"work/transpiled/\"]\n" +
            " :src \"src/\"\n" +
            " :launch-browser false}";

    private static final String DEV_COMPILE_OPTS = "^{}\n" +
            "{:preloads [APP_NAME_UNDERSCORE.core]\n" +
            " :main \"APP_NAME_UNDERSCORE.core\"\n" +
            " :output-to \"work/transpiled/main.js\"\n" +
            " :output-dir \"work/transpiled\"\n" +
            " :asset-path \"work/transpiled\"\n" +
            " :optimizations :none\n" +
            " :verbose true\n" +
            " :static-fns true\n" +
            " :warnings true\n" +
            " :watch-fn (fn [] (println \"TEST BUILD!\"))\n" +
            " :foreign-libs {}}";

    private static final String PROD_COMPILE_OPTS = "^{}\n" +
            "{:preloads [APP_NAME_UNDERSCORE.core]\n" +
            " :main \"APP_NAME_UNDERSCORE.core\"\n" +
            " :output-to \"work/transpiled/main.js\"\n" +
            " :output-dir \"work/transpiled\"\n" +
            " :asset-path \"work/transpiled\"\n" +
            " :optimizations :advanced\n" +
            " :verbose true\n" +
            " :static-fns true\n" +
            " :warnings true\n" +
            " :watch-fn (fn [] (println \"TEST BUILD!\"))\n" +
            " :foreign-libs {}}";

    private static final String APP_CORE = "(ns APP_NAME.core\n" +
            "  (:require [reagent.core :as r]\n" +
            "            [re-frame.core :as rf]\n" +
            "            [goog.events :as gevents]\n" +
            "            [goog.history.EventType :as HistoryEventType]\n" +
            "            [secretary.core :as secretary :refer-macros [defroute]]\n" +
            "            [APP_NAME.views.core :as views]\n" +
            "            [APP_NAME.events.core :as events]\n" +
            "            [APP_NAME.subs.core :as subs])\n" +
            "  (:import goog.History))\n" +
            "\n" +
            "(defn hook-browser-navigation! []\n" +
            "  (doto (History.)\n" +
            "        (gevents/listen\n" +
            "         HistoryEventType/NAVIGATE\n" +
            "         (fn [event]\n" +
            "           (secretary/dispatch! (.-token event))))\n" +
            "        (.setEnabled true)))\n" +
            "\n" +
            "(def APP_NAME-views\n" +
            "  {:home #'views/root-view\n" +
            "   :about #'views/about-page})\n" +
            "\n" +
            "(defn rendered-page\n" +
            "\n" +
            "  []\n" +
            "  [:div\n" +
            "   [(APP_NAME-views @(rf/subscribe [::subs/rendered-page]))]])\n" +
            "\n" +
            ";; **** ROUTES  ****\n" +
            "\n" +
            "(secretary/set-config! :prefix \"#\")\n" +
            "\n" +
            "(defroute \"/about\" {:as params}\n" +
            "  (println \"NAVIGATE About-> \" params)\n" +
            "  (rf/dispatch [::events/navigate :about]))\n" +
            "\n" +
            ";; ******** SET UP ********\n" +
            "\n" +
            "(defn mount\n" +
            "\n" +
            "  []\n" +
            "  (r/render [#'rendered-page] (.getElementById js/document \"app\")))\n" +
            "\n" +
            "(defn ^:after-load re-render\n" +
            "\n" +
            "  []\n" +
            "  (println \"*** Re-loading UI! ***\")\n" +
            "  (mount))\n" +
            "\n" +
            "(defn ^:export main\n" +
            "  \"THE MAIN FUNCTION TO RUNNIT!!!\"\n" +
            "  []\n" +
            "  (enable-console-print!)\n" +
            "  (println \"*** Loading UI! ***\")\n" +
            "  (rf/dispatch-sync [::events/initialize-db])\n" +
            "  (hook-browser-navigation!)\n" +
            "  (mount))";

    private static final String VIEW_CORE_CONTENTS = "(ns APP_NAME.views.core)\n" +
            "\n" +
            "\n" +
            "(defn hero-section\n" +
            "  \"\"\n" +
            "  [title subtitle]\n" +
            "  [:section.hero\n" +
            "   [:div.hero-body\n" +
            "    [:div.container\n" +
            "     [:h1.title title]\n" +
            "     [:h2.subtitle subtitle]]]])\n" +
            "\n" +
            "(defn navbar\n" +
            "  \"\"\n" +
            "  []\n" +
            "  [:nav.navbar {:id \"portal-navbar\"}\n" +
            "   [:div.navbar-brand\n" +
            "    [:a.navbar-item {:href \"/\"} [:span.icon [:i.fas.fa-fire]]]]\n" +
            "   [:div.navbar-menu\n" +
            "    [:div.navbar-start\n" +
            "     [:a.navbar-item {:href \"#/about\"} \"About\"]]\n" +
            "    [:div.navbar-end\n" +
            "     [:a.navbar-item {:href \"#/dead-one\"} \"Dead1\"]\n" +
            "     [:a.navbar-item {:href \"#/dead-two\"} \"Dead2\"]\n" +
            "     [:a.navbar-item {:href \"#/dead-three\"} \"Dead3\"]\n" +
            "     [:a.navbar-item {:href \"#/dead-four\"} \"Dead4\"]\n" +
            "     [:a.navbar-item {:href \"#/portal\"} [:span.icon [:i.fas.fa-balance-scale]]]]]])\n" +
            "\n" +
            "\n" +
            "(defn text-section\n" +
            "  \"\"\n" +
            "  [title subtitle]\n" +
            "  [:section.section\n" +
            "   [:div.container\n" +
            "    [:h1.title title]\n" +
            "    [:br]\n" +
            "    [:h2.subtitle subtitle]\n" +
            "    [:p \"Purpose:\"]]])\n" +
            "\n" +
            "(defn title-and-content\n" +
            "  \"\"\n" +
            "  [title content]\n" +
            "  [:div\n" +
            "    [:strong title]\n" +
            "    [:br]\n" +
            "    [:p content]])\n" +
            "\n" +
            "(defn footer\n" +
            "  \"\"\n" +
            "  []\n" +
            "  [:footer.footer\n" +
            "   [:div\n" +
            "    [:p.has-text-centered\n" +
            "     [:strong \"APP_NAME\"] \" is a work in progress\"]]])\n" +
            "\n" +
            "(defn root-view\n" +
            "  \"\"\n" +
            "  []\n" +
            "  [:div\n" +
            "   (navbar)\n" +
            "   (hero-section \"BAMMMMMMM!!!!\" \"Home Page\")\n" +
            "   [:div.columns\n" +
            "    [:div#cl.column.has-text-centered.has-background-light\n" +
            "     [:span.icon.is-large [:i.fas.fa-3x.fa-landmark]]\n" +
            "     (title-and-content \"Content1\" \"Here is a section for highlighting content... one\")]\n" +
            "    [:div#cl.column.has-text-centered.has-background-light\n" +
            "     [:span.icon.is-large [:i.fas.fa-3x.fa-chess]]\n" +
            "     (title-and-content \"Content2\" \"Here is a section for highlighting content... two\")]\n" +
            "    [:div#cl.column.has-text-centered.has-background-light\n" +
            "     [:span.icon.is-large [:i.fas.fa-3x.fa-drafting-compass]]\n" +
            "     (title-and-content \"Content3\" \"Here is a section for highlighting content... three\")]\n" +
            "    [:div#cl.column.has-text-centered.has-background-light\n" +
            "     [:span.icon.is-large [:i.fas.fa-3x.fa-mercury]]\n" +
            "     (title-and-content \"Content4\" \"Here is a section for highlighting content... four\")]]\n" +
            "   (footer)])\n" +
            "\n" +
            "(defn about-page\n" +
            "\n" +
            "  []\n" +
            "  [:div\n" +
            "   (navbar)\n" +
            "   (hero-section \"APP_NAME\" \"About\")\n" +
            "   (text-section \"What is this site?\" \"Not much yet... do not stop until you're DONE!!!!\")\n" +
            "   (footer)])";

    private static final String EVENTS_CORE_CONTENTS = "(ns APP_NAME.events.core\n" +
            "  (:require [re-frame.core :as rf]\n" +
            "            [ajax.core :as ajax]\n" +
            "            [secretary.core :as secretary]\n" +
            "            [day8.re-frame.http-fx]))\n" +
            "\n" +
            "(rf/reg-event-db\n" +
            " ::initialize-db\n" +
            " (fn [_ _]\n" +
            "   (println \"Initializing DB\")\n" +
            "   {:rendered-page :home\n" +
            "    :login-error \"NO ERROR\"}))\n" +
            "\n" +
            "(rf/reg-event-db\n" +
            "  ::navigate\n" +
            "  (fn [db [event page]]\n" +
            "    (println \"NAVIGATING \" page \" KW \" (keyword? page))\n" +
            "    (assoc db :rendered-page page)))";

    private static final String SUBS_CORE_CONTENTS = "(ns APP_NAME.subs.core\n" +
            "  (:require [re-frame.core :as rf]))\n" +
            "\n" +
            "(rf/reg-sub\n" +
            " ::rendered-page\n" +
            " (fn [db]\n" +
            "   (:rendered-page db)))\n";

    private static final String INDEX_CONTENTS = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "  <head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
            "    <link href=\"/resources/public/css/style.css\" rel=\"stylesheet\" type=\"text/css\">\n" +
            "    <link rel=\"stylesheet\" href=\"https://use.fontawesome.com/releases/v5.8.2/css/all.css\" integrity=\"sha384-oS3vJWv+0UjzBfQzYUhtDYW+Pj2yciDJxpsK1OYPAYjqT085Qq/1cq5FLXAZQ7Ay\" crossorigin=\"anonymous\">\n" +
            "    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/bulma/0.7.4/css/bulma.min.css\" type=\"text/css\">\n" +
            "    <link rel=\"icon\" href=\"https://clojurescript.org/images/cljs-logo-icon-32.png\">\n" +
            "    <title>APP_NAME_UNDERSCORE Index Page</title>" +
            "  </head>\n" +
            "  <body>\n" +
            "    <div id=\"app\"></div>\n" +
            "    <!-- include your ClojureScript at the bottom of body like this -->\n" +
            "    <script src=\"work/transpiled/main.js\" type=\"text/javascript\"></script>\n" +
            "    <script>APP_NAME_UNDERSCORE.core.main();</script>\n" +
            "  </body>\n" +
            "</html>";

    private static final String ROBOTS_CONTENT = "User-agent: *\n" +
            "Disallow: /";
    private static final String STYLES_CONTENT = "body {\n" +
            "background-color: #015668;\n" +
            "}\n";

    public static Map<String, String> contentsMap;

    static {
        contentsMap = new HashMap<>();
        contentsMap.put("deps.edn", DEPS_CONTENTS);
        contentsMap.put("repl.edn", REPL_OPTS);
        contentsMap.put("compile-dev.edn", DEV_COMPILE_OPTS);
        contentsMap.put("compile-prod.edn", PROD_COMPILE_OPTS);
        contentsMap.put("app-core", APP_CORE);
        contentsMap.put("events-core", EVENTS_CORE_CONTENTS);
        contentsMap.put("views-core", VIEW_CORE_CONTENTS);
        contentsMap.put("subs-core", SUBS_CORE_CONTENTS);
        contentsMap.put("index.html", INDEX_CONTENTS);
        contentsMap.put("robots.txt", ROBOTS_CONTENT);
        contentsMap.put("styles.css", STYLES_CONTENT);
    }
}
