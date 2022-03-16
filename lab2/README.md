# Lab 2
Najlepszy sprawdzony przeze mnie zestaw argumentów to:
```bash
-Xms128m -Xmx256m -XX:-ShrinkHeapInSteps -XX:UseSerialGC
```

Aplikacja uruchomiona z tymi argumentami była czasem czyszczona przez GC.
Opcja `-ShrinkHeapInSteps` zapobiega częściowemu czyszczeniu sterty, dzięki czemu docelowa jej wielkość jest osiągana po pierwszym uruchomieniu GC.
Wg dokumentacji Javy najlepszym GC dla mniejszych aplikacji (zajmujących poniżej 100MB) jest UseSerialGC, jest to zgodne z przeprowadzonymi testami (ten GC był uruchamiany częściej).
