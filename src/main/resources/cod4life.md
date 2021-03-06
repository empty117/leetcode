# 策略
- 每轮游戏开始前，对所有sample按照一定策略排序，可保证分数大的优先，或者project需求的优先，可灵活调整.

## Sample排序策略
- 默认按照分数大小排序，优先处理分数更大的.
- 如果需求最小的project只需要<=3个type,则以project的需求排序.
    - 如果手上的type不是project需要的，则按默认排序.
    - 如果project的需求大于1个,则它们按默认排序.

## SAMPLES
- 每次取满3个是一定的，当round>=179时，只剩不到20个,需要12+N步,需要精确控制,只取1个,详见getLevel
    - 180-183时, 6<=N<=8,可以尝试拿1个3,1个2.
    - 183以上时, N<=5,只能尝试拿1个3.
    - 185以上时, N<=3,只能尝试拿1个2.

## DIAGNOSIS
- 如果是从MOLECULES返回的，则说明之前一个sample都没有ready，可能是资源不够，可能是拿满了10个,此时应该选择一个放回cloud.
优先放弃差的最多的sample

- 对于未分析的sample无脑connect进行分析
- 对于已分析自己持有的sample:
    - 如果自己已经拿满10个，就比较惨,如果这个sample没有ready，只有放回cloud.
    - 判断sample是否合格，不合格放回cloud.
- 对于cloud里的sample:
    - 如果自己已经拿满10个，判断当前sample是否可以ready，如果可以则拿起.
    - 如果手上的sample小于3个，且当前sample合格,则拿起
    - 如果round大于180，且手上已经有至少1个了，则不拿了
- 去哪?
    - 手上有ready sample,直接去LAB释放.
    - 如果cloud有>=2个无法拿起的sample,而且可以的最少type少于2个, minCarryNumber = 1;
    - 手上有>=X个合格的sample，则去MOLECULES,反之去SAMPLE继续拿

## MOLECULES(核心模块)
- 如果是自己手上：
    - 判断拿满10个的情况下是否够当前sample，不够就跳过
    - 从当前sampl需求的type里，选出总需求量最大的type，优先取
- 走之前:
    - 如果有1个以上的ready Sample，它们可能是either ready
    - 如果是either ready,优先拿readySample需求的,尽可能达到both ready
    - 如果不能达到both ready，则去LAB
    - 如果能达到both ready，尝试拿未ready的需要的type
    - 最后如果还是没有ready sample:
        - 可能拿满了10个, 只有回DIAGNOSIS放一个或者置换
        - 或者资源不够, 此时可以等待敌人释放或者回DIAGNOSIS置换
            - 判断敌人下一步有没有可能释放够想要的type 数量

## LABORATORY
按sample顺序释放ready sample
- 如果手里只剩1个sample切分数小于30分, 或者round>180且手里没有sample:
    - 如何敌人不会竞争cloud里的sample,而且cloud里有足够的valid sample，则去DIAGNOSIS cloud里拿
    - 否则去SAMPLES
- 看看手里的未ready sample如果回MOLECULES是否能ready
    - 如果有至少1个sample可以ready，则回MOLECULES
    - 否则根据cloud情况回DIAGNOSIS或者SAMPLES


## Static Valid Sample判断
- sample对robot的需求每个type数量<=5
- sample对robot的需求总数<=10

## Env Valid Sample判断
- Sample对robot的需求的每个type数量小于期望资源数
    - 期望资源数 = 实际资源数 + 敌人将要加减的数量

## 敌人将要加减的数量
- 敌人如果target LABORATORY，他也许将要释放掉它的ready Samples对应的所有type
- 敌人如果target在 MOLECULES：
    - 他也许拿一些就会去LABORATORY释放ready sample
        - 预判他潜在的ready sample,对应的type可能随后被释放
    - 他也许不拿潜在的ready sample需要的，而敌人正在防守,拿我需要的?
    - 他也许正在死等
    
## getLevel
- 判断需要take的sample的rank
    - 技能等级<4,拿1
    - 如果手上拿满了而且没有ready sample,拿1
    - 技能等级4-6,可以拿1个2,2个1
    - 技能等级7-9,1个3,2个2.
    - 技能等级>9, 2个3,1个2.

###############################################################################

## 防守策略

如果敌人手头有未ready sample且他的目标在MOLECULES:
- 分析出他每个未ready sample需要的各个type数量
- 确认资源池里的type是否满足他的需求
    - 如果满足,看自己是否能先一步拿走他需要的
        - 如果可以，开始防守
            - 自己优先拿他需要的还是自己需要的？？
            - 如果他需要的就是自己需要的，则正好直接拿
            - 如果不是，判断拿了他的对自己的影响,
                - 如果会影响，导致自己无法产生ready sample，则放弃防守
    - 如果不满足,则证明在自己手里
        - 如果自己将要释放掉他需求的，开始防守,在LABORATORY优先释放其他的ready sample，然后等待
            - 等待敌人的target 不在MOLECULES了,释放目标sample

 如果自己ready sample拿满了，手头还可以拿，看看敌人需求什么最多
 - 如果敌人需求的还有，就拿完或者拿到10个
    - 如果敌人手上需求某个type的sample数量>1,则去拿他需求最多的
 - 优先拿自己需要的还是敌人需要的?
