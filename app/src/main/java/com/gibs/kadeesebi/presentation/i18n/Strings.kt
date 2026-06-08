package com.gibs.kadeesebi.presentation.i18n

import androidx.compose.runtime.compositionLocalOf
import com.gibs.kadeesebi.domain.model.AppLanguage
import com.gibs.kadeesebi.domain.model.BuiltInCircles
import com.gibs.kadeesebi.domain.model.BuiltInEventTypes
import com.gibs.kadeesebi.domain.model.Circle
import com.gibs.kadeesebi.domain.model.EventType
import java.util.Locale

interface Strings {
    val language: AppLanguage
    val locale: Locale

    fun eventTypeName(type: EventType): String = when (type.builtInKey) {
        BuiltInEventTypes.BIRTHDAY -> builtinBirthday
        BuiltInEventTypes.NEW_YEAR -> builtinNewYear
        BuiltInEventTypes.OTHER -> builtinOther
        else -> type.name
    }

    fun circleName(circle: Circle): String = when (circle.builtInKey) {
        BuiltInCircles.FRIEND -> circleFriend
        BuiltInCircles.COLLEAGUE -> circleColleague
        BuiltInCircles.RELATIVE -> circleRelative
        else -> circle.name
    }

    val save: String
    val cancel: String
    val add: String
    val delete: String
    val back: String
    val edit: String
    val ok: String

    val navTois: String
    val navPeople: String
    val navAnalytics: String
    val navSettings: String

    val addToi: String
    val ownToiSuffix: String
    val emptyToisTitle: String
    val emptyToisBody: String

    val newToi: String
    val editToi: String
    val eventType: String
    val eventTitle: String
    val ownToiSwitch: String
    val guestToiSwitch: String
    val whoseToi: String
    val addPeopleFirst: String
    val datePrefix: String
    val place: String
    val notes: String
    val deleteToi: String

    val toiFallback: String
    val addGift: String
    val hostLabel: String
    val placeLabel: String
    val giftsTitle: String
    val emptyGiftsToi: String
    val giftReceivedShort: String
    val giftGivenShort: String
    val newGift: String
    val addPeopleFirstDot: String
    val person: String
    val amount: String
    fun korimdikCollected(amount: String): String
    fun youGaveTotal(amount: String): String

    val cardFallback: String
    val balanceTitle: String
    val gaveToYou: String
    val youGavePerson: String
    val balance: String
    val giftHistory: String
    val emptyGiftsPerson: String
    val addToiFirst: String
    val toi: String
    fun suggestedFuture(amount: String): String

    val addPerson: String
    val emptyPeopleTitle: String
    val emptyPeopleBody: String
    val noCircle: String
    val newPerson: String
    val name: String
    val circle: String
    val relationLabel: String

    val yearlyToiExpenses: String
    val byEventType: String
    val addGiftsForAnalytics: String
    fun gaveToYouAmount(amount: String): String
    fun toisPerYear(count: Int): String

    val shareTitle: String
    val backupSaved: String
    val backupSaveFailed: String
    val dataRestored: String
    val restoreFailed: String
    val saved: String
    val currencyTitle: String
    val currencyBody: String
    val currencyRuble: String
    val currencyTenge: String
    val currencyDollar: String
    val onboardingCurrencyHint: String
    val deletePerson: String
    val deletePersonConfirm: String
    val exportTitle: String
    val exportBody: String
    val exportCsv: String
    val exportPdf: String
    val backupTitle: String
    val backupBody: String
    val createBackup: String
    val restoreBackup: String
    val languageTitle: String
    val languageRussian: String
    val languageKazakh: String
    val languageEnglish: String

    val themeTitle: String
    val themeSystem: String
    val themeLight: String
    val themeDark: String

    val onboardingWelcomeBody: String
    val onboardingThemeHint: String
    val onboardingEventTypesTitle: String
    val onboardingEventTypesHint: String
    val onboardingStart: String

    val reminderTitle: String
    val reminderSwitch: String
    val reminderDateLabel: String
    val reminderTimeLabel: String
    val reminderHint: String

    val aboutTitle: String
    val aboutDeveloperLabel: String
    val aboutVersionLabel: String

    val builtinBirthday: String
    val builtinNewYear: String
    val builtinOther: String

    val circleFriend: String
    val circleColleague: String
    val circleRelative: String

    val circlesTitle: String
    val circlesBody: String
    val manageCircles: String
    val newCircle: String
    val circleNameLabel: String
    val chooseColor: String
    val deleteCircle: String
    val emptyCircles: String

    val eventTypesTitle: String
    val eventTypesBody: String
    val manageEventTypes: String
    val newEventType: String
    val eventTypeNameLabel: String
    val chooseIcon: String
    val deleteEventType: String
    val builtInBadge: String
    val eventTypeInUse: String
    val emptyEventTypes: String

    val photoReceipt: String
    val retakePhoto: String
    val photoReceiptCd: String
    val noCameraApp: String
    val cameraPermissionDenied: String
    val viewReceipt: String
    val removePhoto: String
    val close: String
    val receipt: String
    val noReceipt: String
    val netInBalance: String
    val netTheyAhead: String
    val netYouAhead: String
    val tapToView: String
    val totalGiven: String
    val totalReceived: String
}

object RuStrings : Strings {
    override val language = AppLanguage.RU
    override val locale: Locale = Locale("ru")

    override val save = "Сохранить"
    override val cancel = "Отмена"
    override val add = "Добавить"
    override val delete = "Удалить"
    override val back = "Назад"
    override val edit = "Редактировать"
    override val ok = "OK"

    override val navTois = "События"
    override val navPeople = "Люди"
    override val navAnalytics = "Аналитика"
    override val navSettings = "Настройки"

    override val addToi = "Добавить событие"
    override val ownToiSuffix = "Моё событие"
    override val emptyToisTitle = "Пока нет событий"
    override val emptyToisBody = "Нажмите +, чтобы добавить первое событие и начать вести учёт подарков."

    override val newToi = "Новое событие"
    override val editToi = "Редактировать событие"
    override val eventType = "Тип события"
    override val eventTitle = "Название события (необязательно)"
    override val ownToiSwitch = "  Моё событие"
    override val guestToiSwitch = "  Я гость"
    override val whoseToi = "Чьё событие"
    override val addPeopleFirst = "Сначала добавьте людей на вкладке «Люди»"
    override val datePrefix = "Дата"
    override val place = "Место проведения"
    override val notes = "Заметки"
    override val deleteToi = "Удалить событие"

    override val toiFallback = "Событие"
    override val addGift = "Добавить подарок"
    override val hostLabel = "Чьё событие"
    override val placeLabel = "Место"
    override val giftsTitle = "Подарки"
    override val emptyGiftsToi = "Пока нет записей. Нажмите +, чтобы добавить подарок."
    override val giftReceivedShort = "Подарили"
    override val giftGivenShort = "Подарили вы"
    override val newGift = "Новый подарок"
    override val addPeopleFirstDot = "Сначала добавьте людей на вкладке «Люди»."
    override val person = "Человек"
    override val amount = "Сумма"
    override fun korimdikCollected(amount: String) = "Получено: $amount"
    override fun youGaveTotal(amount: String) = "Вы подарили: $amount"

    override val cardFallback = "Карточка"
    override val balanceTitle = "Баланс взаимности"
    override val gaveToYou = "Подарили тебе"
    override val youGavePerson = "Подарил(а) ты"
    override val balance = "Баланс"
    override val giftHistory = "История подарков"
    override val emptyGiftsPerson = "Пока нет подарков. Нажмите +, чтобы добавить первый."
    override val addToiFirst = "Сначала добавьте событие на вкладке «События»."
    override val toi = "Событие"
    override fun suggestedFuture(amount: String) = "Рекомендуемая сумма на будущее событие: $amount"

    override val addPerson = "Добавить человека"
    override val emptyPeopleTitle = "Пока нет людей"
    override val emptyPeopleBody = "Добавьте родных, друзей и коллег, чтобы вести баланс взаимности."
    override val noCircle = "Без группы"
    override val newPerson = "Новый человек"
    override val name = "Имя"
    override val circle = "Группа"
    override val relationLabel = "Кем приходится (напр. брат, коллега)"

    override val yearlyToiExpenses = "Расходы на подарки за год"
    override val byEventType = "По типам событий"
    override val addGiftsForAnalytics = "Добавьте подарки, чтобы увидеть аналитику расходов."
    override fun gaveToYouAmount(amount: String) = "Подарили тебе: $amount"
    override fun toisPerYear(count: Int) = "Событий за год: $count"

    override val shareTitle = "Поделиться"
    override val backupSaved = "Резервная копия сохранена"
    override val backupSaveFailed = "Не удалось сохранить"
    override val dataRestored = "Данные восстановлены"
    override val restoreFailed = "Не удалось восстановить"
    override val saved = "Сохранено"
    override val currencyTitle = "Валюта"
    override val currencyBody = "Валюта отображения сумм. Влияет только на символ и формат — суммы не пересчитываются по курсу."
    override val currencyRuble = "Рубль \u20bd"
    override val currencyTenge = "Тенге \u20b8"
    override val currencyDollar = "Доллар $"
    override val onboardingCurrencyHint = "Валюту можно изменить позже в настройках."
    override val deletePerson = "Удалить человека"
    override val deletePersonConfirm = "Удалить этого человека и все связанные с ним подарки? Действие необратимо."
    override val exportTitle = "Экспорт"
    override val exportBody = "Выгрузите все подарки в таблицу или PDF."
    override val exportCsv = "Экспорт в CSV (Excel)"
    override val exportPdf = "Экспорт в PDF"
    override val backupTitle = "Резервная копия"
    override val backupBody = "Сохраните все данные в файл и восстановите при необходимости. Без интернета."
    override val createBackup = "Создать резервную копию"
    override val restoreBackup = "Восстановить из копии"
    override val languageTitle = "Язык"
    override val languageRussian = "Русский"
    override val languageKazakh = "Қазақша"
    override val languageEnglish = "English"

    override val themeTitle = "Тема оформления"
    override val themeSystem = "Как в системе"
    override val themeLight = "Светлая"
    override val themeDark = "Тёмная"
    override val onboardingWelcomeBody = "Учитывайте подарки на любые праздники и события: кто, кому и сколько подарил."
    override val onboardingThemeHint = "По умолчанию — как в системе (авто)."
    override val onboardingEventTypesTitle = "Свои события"
    override val onboardingEventTypesHint = "В настройках можно добавить свои типы событий и выбрать для них иконку."
    override val onboardingStart = "Начать"
    override val reminderTitle = "Напоминание"
    override val reminderSwitch = "Напомнить о событии"
    override val reminderDateLabel = "Дата"
    override val reminderTimeLabel = "Время"
    override val reminderHint = "Уведомление придёт в выбранные дату и время."
    override val aboutTitle = "О приложении"
    override val aboutDeveloperLabel = "Разработчик"
    override val aboutVersionLabel = "Версия"

    override val builtinBirthday = "День рождения"
    override val builtinNewYear = "Новый год"
    override val builtinOther = "Другое"
    override val circleFriend = "Друг"
    override val circleColleague = "Коллега"
    override val circleRelative = "Родственник"
    override val circlesTitle = "Группы"
    override val circlesBody = "Создавайте свои группы людей и выбирайте им цвет."
    override val manageCircles = "Управлять группами"
    override val newCircle = "Новая группа"
    override val circleNameLabel = "Название группы"
    override val chooseColor = "Выберите цвет"
    override val deleteCircle = "Удалить группу"
    override val emptyCircles = "Пока нет групп. Добавьте п��рву��."

    override val eventTypesTitle = "Типы событий"
    override val eventTypesBody = "Добавляйте свои типы событий — тои, праздники, что угодно — и выбирайте иконку."
    override val manageEventTypes = "Настроить типы событий"
    override val newEventType = "Новый тип события"
    override val eventTypeNameLabel = "Название типа"
    override val chooseIcon = "Выберите иконку"
    override val deleteEventType = "Удалить тип"
    override val builtInBadge = "Базовый"
    override val eventTypeInUse = "Тип используется в событиях — удалить нельзя"
    override val emptyEventTypes = "Пока нет типов событий."

    override val photoReceipt = "  Фото подарка"
    override val retakePhoto = "  Переснять"
    override val photoReceiptCd = "Фото подарка"
    override val noCameraApp = "На устройстве нет приложения камеры"
    override val cameraPermissionDenied = "Нужен доступ к камере, чтобы сделать фото"
    override val viewReceipt = "Открыть фото подарка"
    override val removePhoto = "Удалить фото"
    override val close = "Закрыть"
    override val receipt = "Подарок"
    override val noReceipt = "Без фото"
    override val netInBalance = "Баланс ровный"
    override val netTheyAhead = "За вами ответный подарок"
    override val netYouAhead = "Вы в плюсе"
    override val tapToView = "Нажмите, чтобы открыть"
    override val totalGiven = "Вы подарили"
    override val totalReceived = "Подарили вам"
}

object KkStrings : Strings {
    override val language = AppLanguage.KK
    override val locale: Locale = Locale("kk")

    override val save = "Сақтау"
    override val cancel = "Болдырмау"
    override val add = "Қосу"
    override val delete = "Жою"
    override val back = "Артқа"
    override val edit = "Өңдеу"
    override val ok = "OK"

    override val navTois = "Оқиғалар"
    override val navPeople = "Адамдар"
    override val navAnalytics = "Талдау"
    override val navSettings = "Параметрлер"

    override val addToi = "Оқиға қосу"
    override val ownToiSuffix = "Өз тойым"
    override val emptyToisTitle = "Әзірге оқиға жоқ"
    override val emptyToisBody = "Алғашқы оқиғаны қосып, сыйлық есебін жүргізу үшін + басыңыз."

    override val newToi = "Жаңа оқиға"
    override val editToi = "О��иғаны өңдеу"
    override val eventType = "Оқиға түрі"
    override val eventTitle = "Оқиға атауы (міндетті емес)"
    override val ownToiSwitch = "  Өз тойым"
    override val guestToiSwitch = "  Мен қонақпын"
    override val whoseToi = "Кімнің оқиғасы"
    override val addPeopleFirst = "Алдымен «Адамдар» қойындысына адам қосыңыз"
    override val datePrefix = "Күні"
    override val place = "Өткізілетін орын"
    override val notes = "Ескертпелер"
    override val deleteToi = "Оқиғаны жою"

    override val toiFallback = "Оқиға"
    override val addGift = "Сыйлық қосу"
    override val hostLabel = "Оқиға иесі"
    override val placeLabel = "Орны"
    override val giftsTitle = "Сыйлықтар"
    override val emptyGiftsToi = "Әзірге жазба жоқ. Сыйлық қосу үшін + басыңыз."
    override val giftReceivedShort = "Берді"
    override val giftGivenShort = "Сіз бердіңіз"
    override val newGift = "Жаңа сыйлық"
    override val addPeopleFirstDot = "Алдымен «Адамдар» қойындысына адам қосыңыз."
    override val person = "Адам"
    override val amount = "Сома"
    override fun korimdikCollected(amount: String) = "Жиналды: $amount"
    override fun youGaveTotal(amount: String) = "Сіз сыйладыңыз: $amount"

    override val cardFallback = "Парақша"
    override val balanceTitle = "Өзара есеп"
    override val gaveToYou = "Саған берді"
    override val youGavePerson = "Сен бердің"
    override val balance = "Баланс"
    override val giftHistory = "Сыйлықтар тарихы"
    override val emptyGiftsPerson = "Әзірге сыйлық жоқ. Алғашқысын қосу үшін + басыңыз."
    override val addToiFirst = "Алдымен «Оқиғалар» қойындысына оқиға қосыңыз."
    override val toi = "Оқиға"
    override fun suggestedFuture(amount: String) = "Болашақ оқиғаға ұсынылатын сома: $amount"

    override val addPerson = "Адам қосу"
    override val emptyPeopleTitle = "Әзірге адам жоқ"
    override val emptyPeopleBody = "Өзара есепті жүргізу үшін туыстарыңыз мен достарыңызды қосыңыз."
    override val noCircle = "Топсыз"
    override val newPerson = "Жаңа адам"
    override val name = "Аты"
    override val circle = "Топ"
    override val relationLabel = "Кім болады (мыс. аға, әріптес)"

    override val yearlyToiExpenses = "Жылдық сыйлық шығыны"
    override val byEventType = "Оқиға түрлері бойынша"
    override val addGiftsForAnalytics = "Шығын талдауын көру үшін сыйлықтар қосыңыз."
    override fun gaveToYouAmount(amount: String) = "Саған берді: $amount"
    override fun toisPerYear(count: Int) = "Жылдық оқиға саны: $count"

    override val shareTitle = "Бөлісу"
    override val backupSaved = "Сақтық көшірме сақталды"
    override val backupSaveFailed = "Сақтау сәтсіз аяқталды"
    override val dataRestored = "Деректер қалпына келтірілді"
    override val restoreFailed = "Қалпына келтіру сәтсіз аяқталды"
    override val saved = "Сақталды"
    override val currencyTitle = "Валюта"
    override val currencyBody = "Сомаларды көрсету валютасы. Тек таңбаға және пішімге әсер етеді — сомалар бағам бойынша қайта есептелмейді."
    override val currencyRuble = "Рубль \u20bd"
    override val currencyTenge = "Теңге \u20b8"
    override val currencyDollar = "Доллар $"
    override val onboardingCurrencyHint = "Валютаны кейін параметрлерде өзгертуге болады."
    override val deletePerson = "Адамды жою"
    override val deletePersonConfirm = "Бұл адамды және оған байланысты барлық сыйлықтарды жою керек пе? Әрекетті болдырмау мүмкін емес."
    override val exportTitle = "Экспорт"
    override val exportBody = "Барлық сыйлықтарды кестеге немесе PDF-ке шығарыңыз."
    override val exportCsv = "CSV (Excel) экспорты"
    override val exportPdf = "PDF экспорты"
    override val backupTitle = "Сақтық көшірме"
    override val backupBody = "Барлық деректі файлға сақтап, қажет болса қалпына келтіріңіз. Интернетсіз."
    override val createBackup = "Сақтық көшірме жасау"
    override val restoreBackup = "Көшірмеден қалпына келтіру"
    override val languageTitle = "Тіл"
    override val languageRussian = "Орысша"
    override val languageKazakh = "Қазақша"
    override val languageEnglish = "English"

    override val themeTitle = "Тақырып"
    override val themeSystem = "Жүйедегідей"
    override val themeLight = "Ашық"
    override val themeDark = "Қараңғы"
    override val onboardingWelcomeBody = "Кез келген мереке мен оқиғадағы сыйлықтарды есепке алыңыз: кім, кімге, қанша."
    override val onboardingThemeHint = "Әдепкі — жүйедегідей (авто)."
    override val onboardingEventTypesTitle = "Өз оқиғаларыңыз"
    override val onboardingEventTypesHint = "Параметрлерде өз оқиға түрлеріңізді қосып, оларға иконка таңдай аласыз."
    override val onboardingStart = "Бастау"
    override val reminderTitle = "Еске салу"
    override val reminderSwitch = "Оқиға туралы еске салу"
    override val reminderDateLabel = "Күні"
    override val reminderTimeLabel = "Уақыты"
    override val reminderHint = "Хабарландыру таңдалған күн мен уақытта келеді."
    override val aboutTitle = "Қолданба туралы"
    override val aboutDeveloperLabel = "Әзірлеуші"
    override val aboutVersionLabel = "Нұсқа"

    override val builtinBirthday = "Туған күн"
    override val builtinNewYear = "Жаңа жыл"
    override val builtinOther = "Басқа"
    override val circleFriend = "Дос"
    override val circleColleague = "Әріптес"
    override val circleRelative = "Туыс"
    override val circlesTitle = "Топтар"
    override val circlesBody = "Өз топтарыңызды құрып, оларға түс таңдаңыз."
    override val manageCircles = "Топтарды басқару"
    override val newCircle = "Жаңа топ"
    override val circleNameLabel = "Топ атауы"
    override val chooseColor = "Түс таңдаңыз"
    override val deleteCircle = "Топты жою"
    override val emptyCircles = "Әзірше топтар жоқ. Біріншісін қосыңыз."

    override val eventTypesTitle = "Оқиға түрлері"
    override val eventTypesBody = "Өз оқиға түрлеріңізді қосып, иконка таңдаңыз."
    override val manageEventTypes = "Оқиға түрлерін баптау"
    override val newEventType = "Жаңа оқиға түрі"
    override val eventTypeNameLabel = "Түр атауы"
    override val chooseIcon = "Иконка таңдаңыз"
    override val deleteEventType = "Түрді жою"
    override val builtInBadge = "Негізгі"
    override val eventTypeInUse = "Түр оқиғаларда қолданылады — жоюға болмайды"
    override val emptyEventTypes = "Әзірге оқиға түрлері жоқ."

    override val photoReceipt = "  Сыйлық фотосы"
    override val retakePhoto = "  Қайта түсіру"
    override val photoReceiptCd = "Сыйлық фотосы"
    override val noCameraApp = "Құрылғыда камера қолданбасы жоқ"
    override val cameraPermissionDenied = "Фото түсіру үшін камераға рұқсат қажет"
    override val viewReceipt = "Сыйлық фотосын ашу"
    override val removePhoto = "Фотоны жою"
    override val close = "Жабу"
    override val receipt = "Сыйлық"
    override val noReceipt = "Фотосыз"
    override val netInBalance = "Баланс тең"
    override val netTheyAhead = "Жауап сыйлық сізде"
    override val netYouAhead = "Сіз плюстесіз"
    override val tapToView = "Ашу үшін басыңыз"
    override val totalGiven = "Сіз сыйладыңыз"
    override val totalReceived = "Сізге сыйлады"
}

object EnStrings : Strings {
    override val language = AppLanguage.EN
    override val locale: Locale = Locale("en")

    override val save = "Save"
    override val cancel = "Cancel"
    override val add = "Add"
    override val delete = "Delete"
    override val back = "Back"
    override val edit = "Edit"
    override val ok = "OK"

    override val navTois = "Events"
    override val navPeople = "People"
    override val navAnalytics = "Analytics"
    override val navSettings = "Settings"

    override val addToi = "Add event"
    override val ownToiSuffix = "My event"
    override val emptyToisTitle = "No events yet"
    override val emptyToisBody = "Tap + to add your first event and start tracking gifts."

    override val newToi = "New event"
    override val editToi = "Edit event"
    override val eventType = "Event type"
    override val eventTitle = "Event name (optional)"
    override val ownToiSwitch = "  My event"
    override val guestToiSwitch = "  I'm a guest"
    override val whoseToi = "Whose event"
    override val addPeopleFirst = "Add people on the People tab first"
    override val datePrefix = "Date"
    override val place = "Venue"
    override val notes = "Notes"
    override val deleteToi = "Delete event"

    override val toiFallback = "Event"
    override val addGift = "Add gift"
    override val hostLabel = "Whose event"
    override val placeLabel = "Place"
    override val giftsTitle = "Gifts"
    override val emptyGiftsToi = "No records yet. Tap + to add a gift."
    override val giftReceivedShort = "Received"
    override val giftGivenShort = "You gave"
    override val newGift = "New gift"
    override val addPeopleFirstDot = "Add people on the People tab first."
    override val person = "Person"
    override val amount = "Amount"
    override fun korimdikCollected(amount: String) = "Received: $amount"
    override fun youGaveTotal(amount: String) = "You gave: $amount"

    override val cardFallback = "Card"
    override val balanceTitle = "Reciprocity balance"
    override val gaveToYou = "Gave to you"
    override val youGavePerson = "You gave"
    override val balance = "Balance"
    override val giftHistory = "Gift history"
    override val emptyGiftsPerson = "No gifts yet. Tap + to add the first one."
    override val addToiFirst = "Add an event on the Events tab first."
    override val toi = "Event"
    override fun suggestedFuture(amount: String) = "Suggested amount for a future event: $amount"

    override val addPerson = "Add person"
    override val emptyPeopleTitle = "No people yet"
    override val emptyPeopleBody = "Add family, friends and colleagues to track your reciprocity balance."
    override val noCircle = "No group"
    override val newPerson = "New person"
    override val name = "Name"
    override val circle = "Group"
    override val relationLabel = "Relation (e.g. brother, colleague)"

    override val yearlyToiExpenses = "Gift spending this year"
    override val byEventType = "By event type"
    override val addGiftsForAnalytics = "Add gifts to see spending analytics."
    override fun gaveToYouAmount(amount: String) = "Gave to you: $amount"
    override fun toisPerYear(count: Int) = "Events this year: $count"

    override val shareTitle = "Share"
    override val backupSaved = "Backup saved"
    override val backupSaveFailed = "Couldn't save"
    override val dataRestored = "Data restored"
    override val restoreFailed = "Couldn't restore"
    override val saved = "Saved"
    override val currencyTitle = "Currency"
    override val currencyBody = "Display currency for amounts. Only changes the symbol and format — amounts are not converted by exchange rate."
    override val currencyRuble = "Ruble \u20bd"
    override val currencyTenge = "Tenge \u20b8"
    override val currencyDollar = "Dollar $"
    override val onboardingCurrencyHint = "You can change the currency later in Settings."
    override val deletePerson = "Delete person"
    override val deletePersonConfirm = "Delete this person and all related gifts? This cannot be undone."
    override val exportTitle = "Export"
    override val exportBody = "Export all gifts to a spreadsheet or PDF."
    override val exportCsv = "Export to CSV (Excel)"
    override val exportPdf = "Export to PDF"
    override val backupTitle = "Backup"
    override val backupBody = "Save all data to a file and restore when needed. Works offline."
    override val createBackup = "Create backup"
    override val restoreBackup = "Restore from backup"
    override val languageTitle = "Language"
    override val languageRussian = "Русский"
    override val languageKazakh = "Қазақша"
    override val languageEnglish = "English"

    override val themeTitle = "Theme"
    override val themeSystem = "System"
    override val themeLight = "Light"
    override val themeDark = "Dark"
    override val onboardingWelcomeBody = "Track gifts for any holiday or event: who gave what, and to whom."
    override val onboardingThemeHint = "Default is system (auto)."
    override val onboardingEventTypesTitle = "Your own events"
    override val onboardingEventTypesHint = "In Settings you can add your own event types and pick an icon for each."
    override val onboardingStart = "Get started"
    override val reminderTitle = "Reminder"
    override val reminderSwitch = "Remind me about this event"
    override val reminderDateLabel = "Date"
    override val reminderTimeLabel = "Time"
    override val reminderHint = "You'll get a notification at the chosen date and time."
    override val aboutTitle = "About"
    override val aboutDeveloperLabel = "Developer"
    override val aboutVersionLabel = "Version"

    override val builtinBirthday = "Birthday"
    override val builtinNewYear = "New Year"
    override val builtinOther = "Other"
    override val circleFriend = "Friend"
    override val circleColleague = "Colleague"
    override val circleRelative = "Relative"
    override val circlesTitle = "Groups"
    override val circlesBody = "Create your own people groups and pick a color for each."
    override val manageCircles = "Manage groups"
    override val newCircle = "New group"
    override val circleNameLabel = "Group name"
    override val chooseColor = "Choose a color"
    override val deleteCircle = "Delete group"
    override val emptyCircles = "No groups yet. Add the first one."

    override val eventTypesTitle = "Event types"
    override val eventTypesBody = "Add your own event types and pick an icon for each."
    override val manageEventTypes = "Manage event types"
    override val newEventType = "New event type"
    override val eventTypeNameLabel = "Type name"
    override val chooseIcon = "Choose an icon"
    override val deleteEventType = "Delete type"
    override val builtInBadge = "Built-in"
    override val eventTypeInUse = "This type is used by events and can't be deleted"
    override val emptyEventTypes = "No event types yet."

    override val photoReceipt = "  Gift photo"
    override val retakePhoto = "  Retake"
    override val photoReceiptCd = "Gift photo"
    override val noCameraApp = "No camera app on this device"
    override val cameraPermissionDenied = "Camera access is needed to take a photo"
    override val viewReceipt = "Open gift photo"
    override val removePhoto = "Remove photo"
    override val close = "Close"
    override val receipt = "Gift"
    override val noReceipt = "No photo"
    override val netInBalance = "Balance is even"
    override val netTheyAhead = "You owe a return gift"
    override val netYouAhead = "You're ahead"
    override val tapToView = "Tap to open"
    override val totalGiven = "You gave"
    override val totalReceived = "Gave to you"
}

fun stringsFor(language: AppLanguage): Strings = when (language) {
    AppLanguage.RU -> RuStrings
    AppLanguage.KK -> KkStrings
    AppLanguage.EN -> EnStrings
}

val LocalStrings = compositionLocalOf<Strings> { RuStrings }
