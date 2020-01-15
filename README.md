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

В последних трёх снимках показаны аргументы для Storage, Storage2 и Storage3

В остальных снимках показаны выводы только что запустившихся программ:
* Как видно, все они сначала сообщают, что успешно запустились
* После этого клиенты ждут команд от пользователя и ничего не выводят
* Хранилища сразу начинают отсылать сообщения NOTIFY прокси
* Прокси их принимает и выводит на экран следующее: если хранилище не зарегистрировано, то выводится сообщение о регистрации, если зарегистировано, то сообщение об обновлении времени таймаута, которое нужно для того, чтобы "убивать" "умершие" хранилища.

## Папка gets

В папке лежат снимки, показывающие, как ведут себя программы при отправке пользователем клиенту команды GET:

* Клиент проверяет правильность команды, если формат команды верный, то он отсылает её прокси, если нет, то выводит сообщение "invalid command" и возможные команды
* Прокси принимает команду, ищет все хранилища, в диапазон которых входит аргумент GET-команды, и отправляет рандомному хранилищу ту же команду, если хранилище не нашлось, то клиент получит сообщение "key not valid"
* Хранилище получает команду GET, забирает по ключу результат и отсылает его прокси командой RESPONSE
* Прокси принимает команду RESPONSE и отправляет её аргументы клиенту
* Клиент получает ответ и выводит его

## Папка puts_n_gets

В папке лежат снимки, показывающие, как ведут себя программы при отправке пользователем клиенту команды PUT:

* Клиент проверяет правильность команды, если формат команды верный, то он отсылает её прокси, если нет, то выводит сообщение "invalid command" и возможные команды
* Прокси принимает команду, ищет все хранилища, в диапазон которых входит аргумент GET-команды, и отправляет рандомному хранилищу ту же команду, если хранилище не нашлось, то клиент получит сообщение "key not valid"
* Хранилище получает команду GET, забирает по ключу результат и отсылает его прокси командой RESPONSE
* Прокси принимает команду RESPONSE и отправляет её аргументы клиенту
* Клиент получает ответ и выводит его
