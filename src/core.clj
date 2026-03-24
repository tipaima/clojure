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

(defn -main []
  (println "Числа Фибоначчи от 0 до 20:")
  (doseq [i (range 21)]
    (println (format "Fib(%2d) = %d" i (fib i)))))

(-main)