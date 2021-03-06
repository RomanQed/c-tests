# c-tests

## О приложении
Консольное приложение, предназначенное для простого управления функциональными тестами согласно формату, принятому
в МГТУ им Н.Э. Баумана.

Поддерживает автоматическое создание тестов на основе входных данных или на основе заданного макро-шаблона.

Обо всех возможностях подробнее см. непосредственно в приложении, команды menu и help.

## Сборка приложения
Для получения исполняемого jar-файла склонируйте репозиторий и выполните gradle-таск shadowJar

```
git clone https://github.com/RomanQed/c-tests.git
cd c-tests
./gradlew shadowJar
```

Готовый к запуску файл будет находиться по пути c-tests/build/libs
В случае запуска в командной строке windows необходимо убедиться, что вы используете кодовую страницу 1251 
(кодировка Windows-1251)
```
C:\Users\Roman>chcp
Текущая кодовая страница: 1251
```

Если кодовая страница другая, смените её на 1251 с помощью chcp

```
C:\Users\Roman>chcp 1251
Текущая кодовая страница: 1251
```

Запускать jar-файл необходимо следующим образом:
```
C:\Users\Roman>java -jar ctests.jar
   ______   ______          __
  / ____/  /_  __/__  _____/ /______
 / /        / / / _ \/ ___/ __/ ___/
/ /___     / / /  __(__  ) /_(__  )
\____/    /_/  \___/____/\__/____/

:
```

В случае успешного запуска будет выведено показанное выше сообщение и приглашение ко вводу ":".

## Язык QMacro
<p>Для макро-подстановок при автоматической генерации тестов используется специально созданный язык - QMacro.</p>
<p>Для получения полного списка поддерживаемых операций см. help macro в приложении</p>

### Спецификация
#### Структура команды
<p>Любая команда, записанная с помощью этого языка, выглядит следующим образом:</p>
<p>{[flag][name]:[arguments]}</p>
<p>[flag] специализирует тип команды</p>
<p>[name] задаёт имя команды</p>
<p>[arguments] задаёт передаваемые аргументы</p>
<p>Разделение аргументов происходит по символу пробела, в случае, когда необходимо задать аргумент, содержащий внутри
пробел, следует использовать круглые скобки: ().</p>
<p>Всё, что находится внутри скобок, будет распознано как единый аргумент, в случае использования скобок внутри скобок, 
внутренние скобки будут проигнорированы.</p>
<p>Таким образом, строка "a (b c (d e)) e f (1 2 3 4)" будет превращена в следующий набор аргументов: 
[a, b c (d e), e, f, 1 2 3 4]</p>
<p>Также, при разборе аргументов все экранирования будут раскрыты.</p>
<p>В случае, если необходимо в одной строке записать более 1 команды, каждую команду, кроме последней,
следует заканчивать символом точки с запятой (;)</p>
<p>Например, {!var:a};{%var}</p>

#### Типы команд
Всего существует 3 типа:
* Объявление переменной, flag='!', возвращаемое значение - пустая строка
* Подстановка переменной, flag='%', возвращаемое значение - содержимое переменной, в случае, если переменная не задана - 
пустая строка
* Вызов определённой средой исполнения команды, flag='/', возвращаемое значение - результат выполнения команды

<p>Пример объявления: {!variable:1}</p>
<p>Пример подстановки: {%variable}</p>
<p>Пример вызова команды: {/int:1 10}</p>

#### Обработка команд
<p>Обработка шаблона происходит слева-направо, от первой строки к последней, в один проход, с сохранением переменных, 
объявленных в предыдущих строках.</p>
<p>Обработка отдельного макроса происходит слева-направо, рекурсивно: каждый аргумент макроса, являющийся макросом,
будет соответственно обработан. Обработка в глубину продолжится до достижения состояния, при котором все аргументы 
текущего макроса макросами являться не будут.</p>
<p>Таким образом, обработка макроса "{!var:({/int:1 10})};{%var}" будет произведена следующим образом:</p>

1) Парсер определит {!var...}; как макрос и начнёт его обработку
2) Парсер определит первый аргумент макроса {!var...}; как макрос {/int:1 10} и начнёт его обработку
3) Макрос {/int...} содержит в себе лишь простые аргументы, следовательно, будет выполнена команда int с аргументами 
\[1, 10] и вернёт случайное число в диапазоне от 1 до 10, например, 3
4) Результат выполнения макроса {/int...} (3) будет подставлен вместо него в ту же позицию, таким образом, 
макрос {!var...} будет иметь следующие аргументы: \[3]
5) Так как остальные аргументы макроса {!var...} макросами не являются, он будет выполнен и создаст в памяти обработчика
переменную с именем "var" и значением "3"
6) Парсер определит следующий макрос в строке, которым окажется {%var}. Он не имеет аргументов, и будет выполнен,
подставив вместо себя содержимое переменной с именем "var". Так как предыдущая команда объявила переменную "var" 
со значением "3", будет подставлено "3"
7) Процесс обработки завершится, и начнётся формирование результата из буфера парсера
8) Сформированный результат, исключая пустые строки, будет возвращён.

#### Примеры подстановок
* Генерация массива случайной длины, содержащего случайные числа
```
{!length:({/int:1 10})}
{%length}
{/arr:{%length} ( ) int -100 100}
```

```
2
-63 66
```

* Генерация прямоугольной матрицы случайно размера, содержащей случайные числа
```
{!n:({/int:1 10})};{!m:({/int:1 10})}
{%n}; {%m}
{/arr:({/i_mul:{%n} {%m}}) ( ) int -100 100}
```

```
3 6
-67 3 30 54 -29 -88 -23 15 11 98 -84 -33 -95 -50 -55 -23 30 -74
```

* Другой способ генерации прямоугольной матрицы (с красивым расположением)
```
{!n:({/int:1 10})};{!m:({/int:1 10})}
{%n}; {%m}
{/arr:{%n} \n arr {%m} ( ) int -100 100}
```

```
7 5
52 19 -28 -62 69
90 -42 -76 -23 -18
25 18 93 69 -17
3 -91 -58 97 -33
24 77 -69 -43 -47
-74 49 88 -25 -56
-69 -3 100 -41 -24
```

* Генерация строки из случайных символов русского алфавита длиной 100
```
{/arr:100 () char А я}
```

```
ЕТЮЖОЪсЬюьзвЙИАпрыоОепнЬщХуРзБЙОЮоКмЗНКШкЦпгРерхЯцдХЙОзлаЮнЫеЩкййЫЦПЫкМЯыэъоыфьххцвУЮИЭмоЧэЯЙсЯЩЙННО
```

* Выбор случайного элемента из последовательности
```
{/che:первый второй третий четвертый пятый}
```

```
третий
```

* Генерация нечётного числа
```
{!length:({/int:2 100})}
{!length:({/if:({/ie:({/mod:{%length} 2}) 0}) ({/i_sub:{%length} 1}) {%length}})}
{%length}
```

```
49
```