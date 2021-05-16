# UPD 1.0

## Сервер
- Постепенный переезд на json
- lombok use
- Чистка ненужных конфигов
- Typo
- Изменения в .xml SA
- Изменения в .xml Skills
- Добавление Bleed/Poison типов эффектов, которые наносят урон в зависимости от сопротивления цели к этим статусам. (Пока не используется н ов будущем, все DamOverTime переедут на этот вариант если DamOverTime является Bleed/Poison типом)
- Рефакторинг
- И много всякого дерьма, которое обычно тут пишут

## Механика
Переработка всех SA в Interlude. Главная цель - сделать SA используемыми по максимуму, т.к. в мои времена Guidance, Evasion брали только не далёкие либо те, у кого не было других SA кристаллов кроме тех, что дают это дерьмо на пушку. **Все SA теперь имеют 1 LEVEL и определяются / накладываются динамически (относится к блидам и похожим эффектам, чтобы не прерывать левел-нить между лоу лвл чаром и хай лвл чаром.)

*Сперва указывается пунктом - SA, подпунктами которого - идут OLD и NEW именно в таком порядке.*

### SA

- Anger
  Increase P.Atk by N and decrease Max HP by 15%.
  Increase P.Atk by 15% and decrease Max HP by 15%.
- Acumen
  Increase casting SPD by 15%
  Increase casting speed by 15% for 1H blunts and for 7.5% for 2H blunts (if has it).
- Back Blow
  Increase critical rate for N while attacking from behind
  Increase critical rate for 172% while attacking from behind
- Cheap Shot
  With N% chance can reduce MP consume to 1 wile attacking from Bow weapon.
  Every Bow attack consumes only 1 MP.
- Crt. Anger
  Adds to P.Atk N value when attack was critical. Absorbs N HP.
  Absorb [LVL / 12 ^ 2] HP and adds 223 + [LVL / 3 ^ 2] P.Atk when attack is critical.
- Crt. Bleed
  With N% can apply bleed on enemy if attack was critical.
  With 70% (100% for blunts) applies bleed on target with power = 10% from P.Atk if attack was critical. Greetings to destroyers and archers! This bleed stacks with another bleed attacks.
- Crt. Drain
  Drain's N HP when attack was critical.
  Drains 40% from critical dealed damage and restores it to HP.
- Crt. Poison
  With N% chance can apply poison on enemy.
  With 70% chance can apply poiosn on target if attack was critical. Power = 66 + 10% M.Atk. This poison stacks with another poisons.
- Crt. Stun
  With N% chance can apply Stun status on enmy if attack was critical.
  With N% chance can apply Stun status on enmy if attack was critical. Also applying Stun skill has a 100% chance of success.
- Empower
  With N% chance can increase Magic power by N.
  Increase M.Atk by [caster LVL * 2.25]
- Evasion
  Increase Evasion by N.
  Increase Evasion by [LVL / 15 ^ 2]
- Focus
  Increase critical rate by N
  Increase critical rate by [56 + (LVL / 8 ^ 2)]
- Guidance
  Increase Accuracy by N
  Increase Accuracy by [LVL / 15 ^ 2]
- Haste
  Increase attack speed by 7%
  Increase attack speed by 15%
- Light
  Decrease weight of weapon by half
  Decrease weight of weapon by full (weapon weight = 0)
- Towering Blow
  Increase attack range by N
  Increase polearm attack range by 54 (and with hallberd stack full attack range will be 120)
- Magic Damage
  Give's 30% chance to deal additional magic damage +8 to M.Atk. (Used by Mother Tree Branch only DB info 146%)
  Increase all magical damage done by 12% (works only for directional spells which can deal dmg)
- Magic debuffs
  - `Paralyze`
    With N% chance when you casting a bad spell, target can be paralyzed.
    With 8% chance when you casting a bad spell, target can be paralyzed by Anchor spell which Magic Level is equals to your current level.
  - `Chaos`
    With N% chance when you casting a bad spell, target can be debuffed by Curse Chaos.
    With 33% chance when you casting a bad spell, target can be debuffed by Curse Chaos spell which Magic Level is equals to your current level.
  - `Weakness`
    With N% chance when you casting a bad spell, target can be debuffed by Curse Weakness.
    With 33% chance when you casting a bad spell, target can be debuffed by Curse Weakness by Anchor spell which Magic Level is equals to your current level.
  - `Hold`
    With N% chance when you casting a bad spell, target can be debuffed by Dryad Root.
    With 13% chance when you casting a bad spell, target can be debuffed by Dryad Root spell which Magic Level is equals to your current level.
  - `Poison`
    With N% chance when you casting a bad spell, target can be poisoned.
    With 20% chance when you casting a bad spell, target can be poisoned which Magic Level is equals to your current level.
  - `Magic Power`
    Increase magic When using magic, increases MP Consumption by 15%, and M. Atk. by N.
    Increase magic power of usable spell by randomly [100-150]%, increases all spells MP Consumption by 50%.
- Wide Blow
  Increase angle of attack for polearm to 120*
  Gives ability with full angle attack 360*
- Rsk. Haste
  Increase attack speed for N% when HP lower than 60%
  Increase attack speed by 1% each 1% of loosed HP when your HP is less than 60%.
- Rsk. Evasion
  Increase evasion by N when HP lower than 60%.
  Increase evasion by 1% for each 1% loosed HP when HP is lower than 60%.
- Rsk. Focus
  Increase critical rate by N when HP lower than 60%.
  Increase critical rate by 1% from max for each 1% loosed HP when HP lower than 60%.
- Miser
  Decrease SS consume when attacking from bow with N% chance
  All shots from bow consumes only 1 SS.
* * *
Пояснение по поводу Rsk. SA.:
Например Evasion.
Ваше HP = 3200
Текущее = 1239
% получаемого Evasion = +35,46875%. Т.е. отсчёт потери HP начинается с момента активации эффекта.
* * *

### Tatoo/Dye

*Татушки изменили своё направление и стремятся к новым горизонтам. Мне нравилось как работает система тату, но меня клинила "ограниченность" в её использовании. Т.е. какие-то тату мы можем ставить, а какие-то нет. Это как?*
Теперь тату делятся на 3 типа:
- Lesser Dye ![Lesser Dye](https://mmo-develop.ru/proxy.php?image=http%3A%2F%2Fl2j.ru%2Finterlude%2Fimg%2Ficons%2Fetc_con_hena_i00_0.png&hash=2a0fb709a970879ae71e736e4b14a7a5)
  - Standart +1 -3 (продаётся у НПЦ)
  - Graced +1 -2 (крафтится)
  - Greater +1 -1 (дроп с боссов)
- Medium Dye ![Medium Dye](https://mmo-develop.ru/proxy.php?image=http%3A%2F%2Fl2j.ru%2Finterlude%2Fimg%2Ficons%2Fetc_con_hena_i01_0.png&hash=7d785dab08f66eac6731bca197938c54)
  - Standart +2 -3 (продаётся у НПЦ)
  - Graced +2 -2 (крафтится)
  - Greater +2 -1 (дроп с боссов)
- Large Dye ![Large Dye](https://mmo-develop.ru/proxy.php?image=http%3A%2F%2Fl2j.ru%2Finterlude%2Fimg%2Ficons%2Fetc_con_hena_i02_0.png&hash=a0d3cd79e83a65487f3f183548a9e0fe)
  - Standart +3 -3 (продаётся у НПЦ)
  - Graced +3 -2 (крафтится)
  - Greater +3 -1 (дроп с боссов)

*Тату больше не имеют ограничителей и теперь любая профессия - может поставить на себя - любое тату.
Теперь все 3 ячейки открыты с самого начала, тут - кто ан что горазд.*

### CP Potions

Без этого не было бы смысла ничего делать вообще. банки ЦП - враг всех PvP режимов в открытом мире. Дворфы могли таскать эти банки тоннами и давать пиздюлей тем классам, которые не вытаскивали демагом - отхил цпшечки. Для оверов и варков это было совсем летально, т.к. они не отжирались от CP врага. Теперь, банки CP работаю так же как и обычные банки хила, но ПОКА ЧТО с базовыми статами, т.е.
- Lesser CP Potion восстанавливает 50 CP но за 5 секунд (10 CP в сек.)
- Greater CP Potion восстанавливает 200 CP тоже за 5 секунд (40 CP в сек.)

## PARAMS

Было проведено феншуйное обновление параметров, которые имели определённые не стыковки. например MEN дающий M.Def в дерьмовом поинте, а CON не дающий P.Def вообще никак. Возможно, это обуславливалось тем, что HP по факту = Защита, если сделать норм расчёт с калькулятором и вывести нужную цифру, но это же Ла2, какие в пизду цифры и расчёты?

- CON - `теперь дополнительно увеличивает и P.Def. Убраны пенальти для игроков которые уменьшали защиту в зависимости от эквипнутого шмота (по аналогии с маг. защитой в MEN). Нам - такие штучки не понадобятся. Изменения CON повлияют на физ. защиту. Она увеличится.`
- MEN
`Магическая защита больше не имеет штрафов при эквипе ювелирии, что позволяет получить чистую M.Def. (Раньше, часть магю защиты срезалась на ABS значения K (нормализующие константы), который нам - не понадобятся.)
Модификатор MENBonus увеличен с 1.010 до 1.016, что позволило получить необходимые показатели как в МП регене/макс МП, так и в даруемой от MEN - маг. защиты. Эти изменения повлияют на максимальные запасы МП, маг. защиту и регенерацию маны. Произойдёт их визуальное увеличение по отношению к базовой Lienage 2.`
- DEX
`Бонус показатель DEX увеличивается с 1.009 до 1.0167, позволяя получить базовый показатель бонуса равный 19% (вместо 10%)
Игрок получает возможность реально видеть как класс растёт с ростом характеристики DEX в "rogue" направление.
Данные изменения повлияют на все параметры и статы получаемые за счёт DEXBonus (lethal rate & shield block rate в них так же входят).`

## CRAFT

*Крафт - больная тема для любого олд фага, особенно, если вы - соло игрок. Качать крафтеров, спойлеров и сутки на пролёт ебашить ресы - не канон. Если вы захотите побачить, можете отправиться в Альбион Онлайн, игра подарит вам потрясающее ощущение от ежедневного говна вроде собери и купи продай. Но Lineage 2 - это о PvP, поэтому грех тратить время на сранный сбор ресурсов (по крайней мере в том кол-ве в котором предоставляла игра на 2006 год, тогда это было норм).*

Задачи для решения:
- Игрок увидев крафт - сразу нажимает exit ибо [А по щам?] оно ему надо? в сотый раз делать то же самое, что он делал уже когда-то.
- Дать кузнецам интересный задаток для начальной игры и будущего геймплея. Чтоб искры из жопы летели.
- Сделать родословную кузнеца особенной в социальном плане - их должен знать весь мир, если они лучшие из лучших.

Ресурсы снижаются до **SQRT[SQRT(CNT) * CNT]**.
Таким образом нам необходимо не *220 Iron Ore* а *57* и т.д. далее с другими ресами.
Большую роль - это сыграет на высоких уровнях, когда игроку нужно овер 10к ресов для крафта пушки с шансом 60-70% успеха.
Вообще упрощение крафта в плане задроства выведено из-за этого ньюанса.
Снижено кол-во необходимых ресурсов для создания изделий
Теперь кузнец имеет *Dwarven Craft 1 Level*. Других у него нет.

Всё ремесло разделилось на 4 типа:
1. Weapon Mastery
2. Armor Mastery
3. Jewel Mastery
4. Other Production

Каждый из спеков ремесла имеет свой уровень и он же сохраняется у игрока. Т.е. создавая Weapon игрок увеличивает опыт Weapon Mastery и уже поднимает уровень ковки именно в этой сфере. Иными словами, кузнецы разделяются на 4 типа, которые используют преимущества друг друга. Т.к. крафт был значительно упрощён в плане необходимого кол-ва ресурсов, данная фича не заставила себя долго ждать.

Спеки кузнеца имеют по 9 уровней, каждый из которых позволит овладеть технологией изготовки того или иного рецепта.
Кузнецы получили собственный рейтинг, который будет объявлятся для любого желающего на доске в любой кузнице (в городах).
Пока что, рейтинг ни на что не влияет и игроки будут просто любоваться собой, но дальше, кузнец станет индивидуумом, который будет иметь спрос на зказы конкретно от игроков, которые не хотят лопатить и гриндить на вторых окнах как отбитые дауны.

В роли гаранта будет выступать система Quote-Order, которая позволит игрокам оплачивать заказы, а кузнецам зарабатывать на стороне.
За каждый успешно скрафченный итем, игрок получает опыт в своём ремесле ( к которому относится созданное изделие).
**/wsminfo** команда позволит кузнецу посмотреть на текущий прогресс. Там же, ему будут поведаны все аспекты механики.

![](http://fin-ex.ru/github_tableofspec.png)