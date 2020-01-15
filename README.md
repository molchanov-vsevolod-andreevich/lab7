# Лабораторная работа №7

В папке logs можно посмотреть логи. Там две папки: в одной скриншоты, в другой текстом то, что выводила IDEA, по-сути, почти то же самое, что в скриншотах. Сейчас расскажу, что к чему.

## Папка start

Запускаются следующие программы:

* Storage
* Storage2
* Storage3
* Client
* Client2
* CentralProxy

В последних трёх снимках показаны аргументы для Storage, Storage2 и Storage3. Storage и Storage3 пересекаются в интервале [0,8].

В остальных снимках показаны выводы только что запустившихся программ:
* Как видно, все они сначала сообщают, что успешно запустились
* После этого клиенты ждут команд от пользователя и ничего не выводят
* Хранилища сразу начинают отсылать сообщения NOTIFY прокси
* Прокси их принимает и выводит на экран следующее: если хранилище не зарегистрировано, то выводится сообщение о регистрации, если зарегистировано, то сообщение об обновлении времени таймаута, которое нужно для того, чтобы "убивать" "умершие" хранилища.

## Папка gets

В папке лежат снимки, показывающие, как ведут себя программы при отправке пользователем клиенту команды GET:

* Клиент проверяет правильность команды, если формат команды верный, то он отсылает её прокси, если нет, то выводит сообщение "invalid command or arguments" и возможные команды
* Прокси принимает команду GET, ищет все хранилища, в диапазон которых входит аргумент GET-команды, и отправляет рандомному хранилищу ту же команду. Если хранилище не нашлось, то клиент получит сообщение "key not valid"
* Хранилище получает команду GET, забирает по ключу результат и отсылает его прокси командой RESPONSE
* Прокси принимает команду RESPONSE и отправляет её аргументы клиенту
* Клиент получает ответ и выводит его.

## Папка puts_n_gets

В папке лежат снимки, показывающие, как ведут себя программы при отправке пользователем клиенту команды PUT:

* Клиент проверяет правильность команды, если формат команды верный, то он отсылает её прокси, если нет, то выводит сообщение "invalid command" и возможные команды
* Прокси принимает команду PUT и отправляет всем хранилищам, в диапазон которых входит аргумент-ключ PUT-команды, ту же команду. После чего отправляет клиенту сообщение "value has been putted", если нашлось хотя бы одно подходящее хранилище, или "key not valid", если хранилище не нашлось
* Хранилище получает команду PUT, записывает по аргументу-ключу аргумент-значение и уничтожает сообщение
* Клиент получает ответ и выводит его.

## Папка storage_off

В папке лежат снимки, показывающие, как Storage отключается, о чём сообщает нам прокси, выводя "storage with id 00CB420C16 has been deleted", соответственно, Storage3 берёт на себя всю нагрузку по интервалу [0,8].

## Папка client_n_storage3_off

В папке лежат снимки, показывающие, как Client и Storage3 отключаются, таким образом, теперь доступен только интервал [11,15], который обслуживает Storage2. Поэтому при запросе по интервалу [0, 10] клиент получает сообщение "key not valid".

## Папка storage_returns

В папке лежат снимки, показывающие, как Storage снова запускается, начинает отправлять команды NOTIFY прокси, который сначала заново регистрирует хранилище, а потом обновляет время его таймаута. Интервал [0,10] снова доступен.

## Папка invalid_commands

В папке лежат снимки, показывающие неверные форматы команд.

## Папка forgot

В папке лежат снимки, показывающие, как программы работают со аргументом-значением команд (строкой), в котором больше одного слова. Я забыл показать этот аспект, поэтому пришлось перезапустить программы.
