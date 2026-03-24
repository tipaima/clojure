(ns core)

(def fib
  (let [cache (atom {0 0, 1 1})]
    (with-meta
      (fn fib
        ([n] (fib n cache))
        ([n cache-ref]
         (if-let [cached (get @cache-ref n)]
           cached
           (let [result (+ (fib (- n 1) cache-ref)
                           (fib (- n 2) cache-ref))]
             (swap! cache-ref assoc n result)
             result))))
      {:cache cache})))

(defn get-fib-cache []
  @(-> fib meta :cache))

(defn -main []
  (println "мемоизация:")
  
  (let [n1 10
        n2 15
        n3 20
        r1 (fib n1)
        r2 (fib n2)
        r3 (fib n3)]
    
    (println (format "Fib(%d) = %d" n1 r1))
    (println (format "Fib(%d) = %d" n2 r2))
    (println (format "Fib(%d) = %d" n3 r3))
    
    (println "\nкэш после вычислений:")
    (let [cache (get-fib-cache)
          sorted-keys (sort (keys cache))]
      (doseq [k sorted-keys]
        (println (format "  fib(%2d) = %d" k (get cache k))))))
)
(-main)