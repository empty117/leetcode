import java.util.*;

/**
 * Bring data on patient samples from the diagnosis machine to the laboratory with enough molecules to produce medicine!
 * TODO:最后的时候，如果自己领先，且敌人手上的sample总数大于差距,尝试防守还是进攻
 *
 **/
class Player {

    private static final String SAMPLES = "SAMPLES", DIAGNOSIS = "DIAGNOSIS", MOLECULES = "MOLECULES", LABORATORY = "LABORATORY";

    private static Robot robot = new Robot();
    private static Robot enemy = new Robot();
    private static String command = "";
    private static int sCount = 0;
    private static Env env = new Env();
    static int THRESHOLD = 181;
    static Project[] projects = new Project[3];
    //从材料台取不够材料
    static boolean giveUp = false;
    //最少从分析台拿够2个才走
    static int minCarryNumber =2;

    //是否开启最终防守模式
    static boolean enableFinalDefend = false;

    static Map<Integer,Sample> sampleInCloud;

    private static void setCommand(String cmd){
        if(command.isEmpty()){
            command = cmd;
        }
    }

//    private static int getRequiredNumber(Robot r,Project project){
//        int total = Math.max(project.a - r.expertiseA,0);
//        total += Math.max(project.b - r.expertiseB,0);
//        total += Math.max(project.c - r.expertiseC,0);
//        total += Math.max(project.d - r.expertiseD,0);
//        total += Math.max(project.e - r.expertiseE,0);
//        return total;
//    }


    static Machine getMachine(String location){
        switch (location){
            case SAMPLES: return new SampleMachine();
            case DIAGNOSIS:return new DiggnosisMachine();
            case MOLECULES:return new MoleculeMachine();
            case LABORATORY:return new LabMachine();
            default:return  new SampleMachine();
        }
    }
    private static int round = 0;
    private static class Project implements Comparable<Project>{
        static boolean enemyUse = false;
        int a,b,c,d,e;
        Project(int a, int b, int c,int d,int e){
            this.a=a;
            this.b=b;
            this.c=c;
            this.d=d;
            this.e=e;
        }

        int getTotal(){
            return a+b+c+d+e;
        }

        int getTotalRequired(Robot robot){
            int count =0;
            for(Type type:Type.types){
                count += getRequiredNumber(robot,type.value);
            }
            return count;
        }

        int getRequiredNumber(Robot robot,String type){
            return Math.max(getTypeNumber(type) - robot.getExpertiseByType(type) ,0);
        }

        int getTypeNumber(String type){
            switch (type){
                case "A":return a;
                case "B":return b;
                case "C":return c;
                case "D":return d;
                case "E":return e;
            }
            return 0;
        }

        @Override
        public int compareTo(Project o) {
            if(enemyUse){
                return getTotalRequired(enemy) - o.getTotalRequired(enemy);
            }
            return getTotalRequired(robot) - o.getTotalRequired(robot);
        }
    }


    //选择需求最少的project需要的type(1分)
    private static int getChosenProjectRequiredScore(Project chosenProject, String type){
        int score;
        int required = chosenProject.getRequiredNumber(robot,type);
        if(required>0 && "ABCDE".contains(type)){
            System.err.println("选择的project一共需要"+chosenProject.getTotalRequired(robot)+"个type,需要"+required+"个type"+type);
        }
        score =  required>0?1:0;
        if("ABCDE".contains(type)){
//            System.err.println("project score for type"+type+"="+score );
        }
        return score;
    }

    private static Project getChosenProject(Robot r, Robot e){
//        System.err.println("project开启敌人排序策略了吗？"+Project.enemyUse);
        Arrays.sort(projects);
//        System.err.println(projects);
        for(int i=0;i<projects.length;i++){
            //需求最少的未完成project
            if(projects[i].getTotalRequired(r)>0 && projects[i].getTotalRequired(e)>0){
                return projects[i];
            }
        }
        return null;
    }

    /*
    优先级最高的project的type 3分
    优先级第二高的project的type 2分
    优先级最低的project的type 1分
     */
    private static int getProjectRequiredScore(String type){
        int score =0 ;
        Arrays.sort(projects);
        for(int i=0;i<projects.length;i++){
            if(projects[i].getTotalRequired(robot)>0 && projects[i].getTotalRequired(enemy)>0){
                int required = projects[i].getRequiredNumber(robot,type);
                if(required>0){
                    score+=(3-i);
                    if("ABCDE".contains(type)){
                        System.err.println("project["+i+"]一共需要"+projects[i].getTotalRequired(robot)+"个type,需要"+required+"个type"+type);
                    }
                }
            }
        }
        if("ABCDE".contains(type)){
            System.err.println("project score for type"+type+"="+score );
        }

        return score;
    }

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int projectCount = in.nextInt();
        for (int i = 0; i < projectCount; i++) {
            int a = in.nextInt();
            int b = in.nextInt();
            int c = in.nextInt();
            int d = in.nextInt();
            int e = in.nextInt();
            projects[i] = new Project(a,b,c,d,e);
            System.err.println("project"+i+",a="+a+",b="+b+",c="+c+",d="+d+",e="+e);
        }
        // game loop
        while (true) {
            command = "";
            round++;
            if(round<THRESHOLD){
                minCarryNumber = 2;
            }
            Robot[] robots= new Robot[2];
            for (int i = 0; i < 2; i++) {
                String target = in.next();
                int eta = in.nextInt();
                int score = in.nextInt();
                int storageA = in.nextInt();
                int storageB = in.nextInt();
                int storageC = in.nextInt();
                int storageD = in.nextInt();
                int storageE = in.nextInt();
                int expertiseA = in.nextInt();
                int expertiseB = in.nextInt();
                int expertiseC = in.nextInt();
                int expertiseD = in.nextInt();
                int expertiseE = in.nextInt();
                robots[i] = new Robot();
                robots[i].expertiseA = expertiseA;
                robots[i].expertiseB = expertiseB;
                robots[i].expertiseC = expertiseC;
                robots[i].expertiseD = expertiseD;
                robots[i].expertiseE = expertiseE;
                robots[i].eta = eta;
                robots[i].score = score;
                robots[i].location=target;
                robots[i].countA = storageA;
                robots[i].countB = storageB;
                robots[i].countC = storageC;
                robots[i].countD = storageD;
                robots[i].countE = storageE;
            }
            robot = robots[0];
            enemy = robots[1];
            int availableA = in.nextInt();
            int availableB = in.nextInt();
            int availableC = in.nextInt();
            int availableD = in.nextInt();
            int availableE = in.nextInt();
            env.a = availableA;
            env.b = availableB;
            env.c = availableC;
            env.d = availableD;
            env.e = availableE;

            int sampleCount = in.nextInt();
            sCount = sampleCount;
            String location = robot.location;
            sampleInCloud = new HashMap<>();
            robot.getSamples().clear();
            enemy.getSamples().clear();
            List<Sample> sampleList = new ArrayList<>();
            for (int i = 0; i < sampleCount; i++) {
                int sampleId = in.nextInt();
                int carriedBy = in.nextInt();
                int rank = in.nextInt();
                String expertiseGain = in.next();
                int health = in.nextInt();
                int costA = in.nextInt();
                int costB = in.nextInt();
                int costC = in.nextInt();
                int costD = in.nextInt();
                int costE = in.nextInt();

                Sample sample = new Sample(sampleId, carriedBy, health, rank, costA, costB, costC, costD, costE,expertiseGain);
                if(sample.getCarriedBy() == -1){
                    sampleInCloud.put(sampleId,sample);
                }
                else if(sample.getCarriedBy() == 1){
                    enemy.getSamples().put(sampleId,sample);
                }
                else if(sample.getCarriedBy() == 0){
                    robot.getSamples().put(sampleId,sample);
                }
                sampleList.add(sample);
            }
            Machine machine = getMachine(location);
            System.err.println("robot在"+robot.getLocation()+",距离为"+robot.eta);
            System.err.println("robot持有"+robot.getMoleculeCount()+"个molecule,"+robot.getSamples().size()+"个sample");
//            System.err.println("Before的命令是:" + command);
            machine.before();
            sampleList.sort(Sample::compareTo);
            for(Sample sample: sampleList){
                System.err.println(sample);
                machine.execute(sample);
            }
//            System.err.println("Execute的命令是:" + command);
            machine.executeFinal();
//            System.err.println("Final的命令是:" + command);
            System.out.println(command);

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
        }
    }

    interface Machine{
        void before();
        void execute(Sample sample);
        void executeFinal();
    }

    /**
     * 尝试取一个sample
     * 如果取满3个去分析台
     */
    private static class SampleMachine implements Machine{

        private int level;

        @Override
        public void before() {
            if(!robot.location.equals(SAMPLES) || robot.eta>0){
                setCommand("GOTO " + SAMPLES);
                return;
            }
            level = getLevel();
            System.err.println("round="+round+",level="+level);
            if(round >=THRESHOLD){
                minCarryNumber = 1;
                if(round > 185){
                    level = level == 3?2:level;
                }
                else if(level==3 && robot.getSamples().size()==1){
                    level = 2;
                }
                System.err.println("最后一次拿"+level);

                List<Sample> destroySamples = enemyLeadSuprPassSamples();
                if(!destroySamples.isEmpty()){
                    enableFinalDefend = true;
                    setCommand("GOTO " + MOLECULES);
                }
                else{
                    Project.enemyUse = false;
                }
            }
            if(sCount == 0){
                setCommand("CONNECT " + level);
            }
        }

        @Override
        public void execute(Sample sample) {
        }

        @Override
        public void executeFinal() {
            //如果没拿满
            if(robot.getSamples().size()<3){
                //最后几步了，拿起就走
                if(round>=THRESHOLD && robot.getSamples().size()>0){
                    boolean hasUndiagnosed = false;
                    for(Sample sample:robot.getSamples().values()){
                        if(sample.getState() == State.undiagnosed){
                            hasUndiagnosed = true;
                            break;
                        }
                    }
                    //有至少一个未分析的
                    if(hasUndiagnosed && round >= THRESHOLD+2){
                        setCommand("GOTO " + DIAGNOSIS);
                    }
                    if(round>=THRESHOLD && robot.getSamples().size()==2){
                        setCommand("GOTO " + DIAGNOSIS);
                    }
                    else{
                        setCommand("CONNECT " + level);
                    }
                }
                else{
                    if(robot.getSamples().size() == 2){
                        if(robotCanTakeSampleNumberInCloud()>0){
                            setCommand("GOTO " + DIAGNOSIS);
                        }
                        else{
                            setCommand("CONNECT " + level);
                        }
                    }
                    else{
                        setCommand("CONNECT " + level);
                    }

                }
            }
            else{
                setCommand("GOTO " + DIAGNOSIS);
            }
        }
    }

    /**
     * 手上未分析的就分析
     * 如果是从材料台回来，就放弃一个
     * 分析过的评估是否值得继续,不值得就放弃
     * 如果手头没满，cloud里有合适的，拿起
     * -- rank小于3的可以用于从材料台回来后补拿
     */
    private static class DiggnosisMachine implements Machine{
        @Override
        public void before() {
            if(robot.eta>0){
                setCommand("GOTO " + DIAGNOSIS);
                return;
            }
             /*
            回来放弃的一定是没有ready的,选一个合适的放回cloud
            可能是拿满了
            可能是资源不够了
             */
            if(giveUp&&command.isEmpty()){
                System.err.println("尝试放弃一个");
                int chosenId=-1, score=0;
                //优先尝试丢差的最多的
                for(Sample s:robot.getSamples().values()){
                    //
                    if(s.getState() == State.diagnosed && (!(s.isStaticValid()&&s.isEnvValid(3)) || robot.getMoleculeCount() == 10)){
                        if(score<robot.getRequiredCount(s)){
                            score = robot.getRequiredCount(s);
                            chosenId = s.getSampleId();
                        }

                    }
                }
                giveUp = false;
                if(chosenId>0){
                    setCommand("CONNECT " + chosenId);
                    return;
                }
            }
        }

        @Override
        public void execute(Sample sample) {
            if(sample.carriedBy==0){
                //未分析的sample
                if(sample.getState() == State.undiagnosed){
                    robot.diagnosSample(sample);
                }
                //如果拿满了且没有ready的，就比较悲剧,只有放下
                else if(robot.getMoleculeCount()==10){
                    if(robot.getRequiredCount(sample)>0){
                        robot.putToCloud(sample.getSampleId());
                    }
                }
                //已分析的
                else{
                    //sample不合格,或者不符合冲刺要求
                    if(!sample.isStaticValid() || !sample.isEnvValid(3)){
                        robot.putToCloud(sample.getSampleId());
                    }
                }
            }
            else if(sample.carriedBy == -1){
                //拿满了
                if(robot.getMoleculeCount() == 10){
                    //如果有够的拿起
                    if(robot.getRequiredCount(sample) == 0){
                        robot.takeFromCloud(sample);
                    }
                }
                else if(sample.isStaticValid() && sample.isEnvValid(3) && robot.getSamples().size()<3){
                    //最后几轮了
                    if(round>THRESHOLD && robot.getSamples().size()>0){
                        return;
                    }
                    robot.takeFromCloud(sample);
                }
            }
        }

        @Override
        public void executeFinal() {
            /*
            全部ready
             */
            if(robot.getSamples().size()>0 && robot.getReadySampleNumber()==robot.getSamples().size()){
                setCommand("GOTO " + LABORATORY);
            }
            /*
            如果资源有限，拿不起
            TODO:目前是cloud里至少有2个拿不起，而且最少的type小于2个
             */
            if((sampleInCloud.size()>=2 && (env.getMinAvailable()< 2)) || round >= THRESHOLD){
                minCarryNumber = 1;
            }
            if(robot.getSamples().size()>= minCarryNumber){
                setCommand("GOTO " + MOLECULES);
            }
            else{
//                limitResource=-1;
                setCommand("GOTO " + SAMPLES);
            }
        }
    }

    /**
     * 要么全部取ready去lab
     * 要么ready一部分，剩下的取不到了或者取满了去lab
     * 要么取不到了等
     * 要么取不到了回去放弃
     */
    private static class MoleculeMachine implements Machine{


        @Override
        public void before() {
            if(robot.eta>0){
                setCommand("GOTO " + MOLECULES);
                return;
            }
        }

        @Override
        public void execute(Sample sample) {
            if(sample.getCarriedBy()==0){
                //如果自己拿不够，先跳过
                if(robot.getRequiredCount(sample) + robot.getMoleculeCount()>10){
                    return;
                }
                int chosenTypeCount = 0;
                Type chosenType = null;
                //自己是ready了
                for(Type type: Type.types){
                    //对于某个type需要的数量
                    ////需要拿而且有
                    if(robot.getRequiredCount(type,sample)>0 && env.getAvailableCountByType(type)>0){
                        //先看下总需求
                        int typeCount = robot.getRequiredCount(type,robot.getSamples().keySet());
                        if(typeCount > chosenTypeCount){
                            chosenTypeCount = typeCount;
                            chosenType = type;
                        }
                    }
                }
                //选择需要的types里面总需求量最大的一个优先take
                if(chosenType!=null){
                    robot.takeMolecule(chosenType);
                }
            }
        }

        @Override
        public void executeFinal() {
            if(!command.isEmpty()){
                return;
            }
            boolean hasReady = false;
            Set<Integer> readySamples = new HashSet<>();
            Set<Integer> noReadySamples = new HashSet<>();
            for(int id:robot.getSamples().keySet()){
                if(robot.getRequiredCount(id) ==0){
                    readySamples.add(id);
                    hasReady = true;
                }
                else{
                    noReadySamples.add(id);
                }
            }

            //开启了最终防守模式
            if(enableFinalDefend){
                tryDefend(enemyLeadSuprPassSamples());
            }
            //如果有至少1个 sample ready了
            else if(hasReady){
                //先尝试防守
                tryDefend(enemyPotentialReadySamples());
            }
            //Ready Samples:  任意一个ready了
            //优先拿readySamples需要的
            for(Type type:Type.types){
                //某个type没有了
                if(env.getAvailableCountByType(type)<=0){
                    continue;
                }
                if(robot.getRequiredCount(type,readySamples)>0){
                    robot.takeMolecule(type);
                    break;
                }
            }

            //所有ready Samples都ready
            boolean allReadySamplesDone = robot.getRequiredCount(readySamples) == 0;
            System.err.println("has readySample:"+hasReady+",readySamples:" + readySamples+",allReadySamplesDone="+allReadySamplesDone);

            /*
            如果ready sample拿不满了
            要么满10个了
            要么资源不够了
             */
            if(hasReady && !allReadySamplesDone){
                setCommand("GOTO " + LABORATORY);
            }

            /*
            ready sample拿满了，该继续进攻还是防守?
             */
            else{
                //尝试拿没ready的
                Set<Type> noReadySamplesRequiredTypes = robot.getRequiredTypes(noReadySamples);
                for(int id:noReadySamples){
                    for(Type type:noReadySamplesRequiredTypes){
                        if(robot.getRequiredCount(id)>0 && env.getAvailableCountByType(type)>0){
                            robot.takeMolecule(type);
                            break;
                        }
                    }
                }
                //都试过了，有sample ready的话就去LAB吧
                if(hasReady){
                    setCommand("GOTO " + LABORATORY);
                }
                //悲剧，都试过了，还没有sample ready,OMG
                else{
                    //拿不动了,回去放
                    if(robot.getMoleculeCount() == 10){
                        if(command.isEmpty()){
                            giveUp = true;
                        }
                        setCommand("GOTO " + DIAGNOSIS);
                    }
                    //TODO:待优化
                    //敌人卡死，则自己比较傻
                    //看能不能等
                    else{
                        boolean canWait = false;
                        for(Sample sample:robot.getSamples().values()){
                            if(sample.isStaticValid() && sample.isEnvValid(0)){
                                canWait = true;
                                break;
                            }
                        }
                        if(canWait){
                            setCommand("WAIT");
                        }
                        else if(robot.getSamples().size()<minCarryNumber){
                            setCommand("GOTO " + SAMPLES);
                        }
                        //不行,放弃
                        else{
                            if(command.isEmpty()){
                                giveUp = true;
                            }
                            setCommand("GOTO " + DIAGNOSIS);
                        }
                    }
                }
            }
        }

        private void tryDefend(List<Sample> defendSamples) {
            if(enemy.getLocation().equals(DIAGNOSIS) || enemy.getLocation().equals(MOLECULES)){
                //敌人可以ready的samples,按照分数高低排序,优先破坏他分数高的
                here:
                for(Sample sample:defendSamples){
                    boolean canReady = enemy.envTypesReadyForSample(sample);
                    boolean capacityAllowReady = enemy.getRequiredCount(sample) + enemy.getMoleculeCount()<=10;
                    System.err.println("Sample"+sample.getSampleId()+"可以ready吗？"+canReady+"容量允许ready吗？"+capacityAllowReady);
                    //无法ready的sample不管
                    //需求sample拿起来超过10个不管
                    if(!canReady || !capacityAllowReady){
                        continue;
                    }
                    int space = 10 - robot.getMoleculeCount();
                    for(Type tt: Type.types){
                        int required = enemy.getRequiredCount(tt,sample);
                        System.err.println("对于sample"+sample.sampleId+",敌人还需要"+required+"个"+tt.value);
                        if(required<=0){
                            continue;
                        }
                        int destryRequired = env.getAvailableCountByType(tt) - required + 1;
                        System.err.println("防守需要"+destryRequired+"个"+tt.value);
                        //开始破坏
                        if(space >= destryRequired && enemy.getMoleculeCount()<10){
                            System.err.println("开始防守,敌人的sample"+sample.getSampleId()+",type="+tt.value());
                            robot.takeMolecule(tt);
                            break here;
                        }
                    }
                }
            }
        }
    }

    private static class LabMachine implements Machine{

        @Override
        public void before() {
            if(robot.eta>0){
                setCommand("GOTO " + LABORATORY);
                return;
            }
        }

        @Override
        public void execute(Sample sample) {
            if(sample.getCarriedBy()==0){
                if(robot.getRequiredCount(sample.getSampleId()) ==0){
                    /*
                    当敌人手上只有一个未ready sample时触发防守:
                    先判断释放了会不会救了敌人
                    如果会就WAIT
                     */
                    if(enemy.getLocation().equals(MOLECULES)
                            && enemy.getMoleculeCount() == 1 && enemy.getReadySampleNumber() ==0){
                        for(Sample sampleForEnemy: enemy.getSamples().values()){
                            if(enemy.getRequiredCount(sampleForEnemy) >0){
                                for(Type type:Type.types){
                                    //如果敌人需求大于存量,敌人可能在等我释放
                                    if(enemy.getRequiredCount(type,sampleForEnemy)> env.getAvailableCountByType(type)){
                                        System.err.println("敌人对于"+sampleForEnemy.sampleId+"需求"+type.value+" " + enemy.getRequiredCount(type,sampleForEnemy)+"个"+",本人即将释放"+sample.getCostForRobot(type.value,robot)+"个");
                                        //如果将要释放的type>=敌人需要的
                                        if(sample.getCostForRobot(type.value,robot)>= enemy.getRequiredCount(type,sampleForEnemy)-env.getAvailableCountByType(type)){
                                            command = "PENDING";
                                        }
                                    }
                                }
                            }
                        }
//                        if(enemy.getReadySampleNumber()==0 && enemy.getSamples().size() > 0){
//                            Sample noReadySample = null;
//                            for(Sample s:enemy.getSamples().values()){
//                                noReadySample = s;
//                                break;
//                            }
//                            for(Type type:Type.types){
//                                //如果当前type没有了，而且敌人需求,敌人可能在等我释放
//                                if(enemy.getRequiredCount(type,noReadySample)>0){
//                                    //如果将要释放的type>=敌人需要的
//                                    if(sample.getCostForRobot(type.value,robot)>= enemy.getRequiredCount(type,noReadySample)){
//                                        pending = true;
//                                        break;
//                                    }
//                                }
//                            }
//                        }
                    }
                    robot.produceOnLab(sample.getSampleId());
                }
            }
        }

        @Override
        public void executeFinal() {
            System.err.println("command is:"+command);
            System.err.println("robot.getReadySampleNumber()="+robot.getReadySampleNumber()+"robot.core="+robot.score+",enemy.score="+enemy.score);
            //如果还有readySample会释放则证明自己在防守
            if(command.equals("PENDING") && robot.getReadySampleNumber()>0 && robot.score > enemy.score){
                System.err.println("开始防守");
                command = "WAIT";
            }
            if(!command.isEmpty()){
                return;
            }

            /*
            如果手头少于一个sample而且分数小于30
            或者最后几轮手里没sample了
             */
            if((robot.getSamples().size()<=1 &&robot.getTotalHealth()<30)
                    || (robot.getSamples().size()==0 && round>=THRESHOLD)){
                //如何敌人不会竞争cloud里的sample
                int robotCanTakeSampleNumberInCloud = robotCanTakeSampleNumberInCloud();
                System.err.println("可以从cloud里拿"+robotCanTakeSampleNumberInCloud+"个");
                if(round>=THRESHOLD){
                    if(!enemyLeadSuprPassSamples().isEmpty()){
                        enableFinalDefend = true;
                        setCommand("GOTO " + MOLECULES);
                    }
                    else{
                        Project.enemyUse = false;
                    }
                }
                if(robotCanTakeSampleNumberInCloud >0 && robotCanTakeSampleNumberInCloud >= 3 - robot.getSamples().size()-1){
                    setCommand("GOTO " + DIAGNOSIS);
                }
                else {
                    setCommand("GOTO " + SAMPLES);
                }
            }
            //手上的sample大于30分或者最后几轮了且手上有至少一个sample
            else{
                boolean canDo = false;
                for(Sample sample:robot.getSamples().values()){
                    boolean sampleCanDo = true;
                    for(Type type:Type.types){
                        //某个不够
                        int requiredNumber = robot.getRequiredCount(type,sample);
                        //资源不够，或者总数超过10个,当前sample只要有一个type不够就放弃这个sample
                        if(env.getAvailableCountByType(type)+enemyGoingToRelease(type,3)<requiredNumber || requiredNumber+robot.getMoleculeCount()>10){
                            sampleCanDo = false;
                            break;
                        }
                    }
                    //只要有一个sample cando，就可以搞
                    if(sampleCanDo){
                        canDo=true;
                        break;
                    }
                }
                //如果MOLECULES的type可能足够
                if(canDo){
                    setCommand("GOTO " + MOLECULES);
                }
                //优先尝试去DIAGNOSIS取
                else if(robotCanTakeSampleNumberInCloud()>=1){
                    setCommand("GOTO " + DIAGNOSIS);
                }
                else{
                    setCommand("GOTO " + SAMPLES);
                }
            }
        }
    }

    static int robotCanTakeSampleNumberInCloud(){
        int count =0;
        for(Sample sample:sampleInCloud.values()){
            if(sample.isStaticValid() && sample.isEnvValid(4)){
                if(enemyCannotTakeFromCloud(sample)){
                    //敌人将要拿cloud里的
                    if(enemy.getLocation().equals(DIAGNOSIS)){
                        continue;
                    }
                    //敌人3步以后将要拿cloud里的
                    if(enemy.getLocation().equals(SAMPLES)&&enemy.getSamples().size()==3){
                        continue;
                    }
                }
                count++;
            }
        }
        return count;
    }

    static int enemyCannotTakeSampleInCloud(){
        int maxCount = 3 - enemy.getSamples().size();
        int count =0;
        for(Sample sample:sampleInCloud.values()){
            //enemy可以搞定的
            if(enemy.envTypesReadyForSample(sample)
                    && enemy.getRequiredCount(sample) <= 10-enemy.getMoleculeCount()){
                count ++;
            }
        }
        return Math.min(count,maxCount);
    }

    static boolean enemyCannotTakeFromCloud(Sample sample){
        if(enemy.getSamples().size()==3 || enemy.getMoleculeCount() == 10){
            return false;
        }
        return enemy.envTypesReadyForSample(sample)
                && enemy.getRequiredCount(sample) <= 10-enemy.getMoleculeCount();
    }



    private static class Sample implements Comparable<Sample>{
        /*
        不可变
         */
        private final int sampleId;
        private final int carriedBy;
        private final int health;
        private final int costA;
        private final int costB;
        private final int costC;
        private final int costD;
        private final int costE;
        private final String expertiseGain;

        private final int rank;
        private State state;


        Sample(int id, int carriedBy, int health,int rank, int costA, int costB, int costC, int costD, int costE,String expertiseGain) {
            this.sampleId = id;
            this.carriedBy = carriedBy;
            this.health = health;
            this.rank = rank;
            this.costA = costA;
            this.costB = costB;
            this.costC = costC;
            this.costD = costD;
            this.costE = costE;
            if(health<1){
                this.state = State.undiagnosed;
            }
            else{
                this.state = State.diagnosed;
            }
            this.expertiseGain = expertiseGain;
        }

        public String getExpertiseGain() {
            return expertiseGain;
        }

        public void setState(State state) {
            this.state = state;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder("sample信息:");
            sb.append("id="+sampleId)
                    .append("carriedBy=" + carriedBy)
                    .append("helth="+health)
                    .append(",rank="+rank)
                    .append(",costA="+costA)
                    .append(",costB="+costB)
                    .append(",costC="+costC)
                    .append(",costD="+costD)
                    .append(",costE="+costE)
                    .append(",state="+state);
            return sb.toString();
        }

        public State getState() {
            return state;
        }

        public int getRank() {
            return rank;
        }

        public int getSampleId() {
            return sampleId;
        }

        public int getCarriedBy() {
            return carriedBy;
        }

        public Integer getHealth() {
            return health;
        }

        public int getCostAForRobot(Robot robot) {
            return Math.max(costA - robot.expertiseA,0);
        }

        public int getCostBForRobot(Robot robot) {
            return Math.max(costB - robot.expertiseB,0);
        }

        public int getCostCForRobot(Robot robot) {
            return Math.max(costC - robot.expertiseC,0);
        }

        public int getCostDForRobot(Robot robot) {
            return Math.max(costD - robot.expertiseD,0);
        }

        public int getCostEForRobot(Robot robot) {
            return Math.max(costE - robot.expertiseE,0);
        }

        public int getCostForRobot(String type, Robot r){
            switch (type){
                case "A":return Math.max(costA - r.expertiseA,0);
                case "B":return Math.max(costB - r.expertiseB,0);
                case "C":return Math.max(costC - r.expertiseC,0);
                case "D":return Math.max(costD - r.expertiseD,0);
                case "E":return Math.max(costE - r.expertiseE,0);
            }
            return 0;
        }

        public int getTotolCostForRobot(Robot robot) {
            return getCostAForRobot(robot) +
                    getCostBForRobot(robot) +
                    getCostCForRobot(robot) +
                    getCostDForRobot(robot) +
                    getCostEForRobot(robot);
        }

        /**
         * 静态合法
         * 需要的type至少有一个可以取
         * @return
         */
        boolean isStaticValid(){
            boolean staticValid = getCostAForRobot(robot)<=5 && getCostBForRobot(robot)<=5
                    && getCostCForRobot(robot)<=5 && getCostDForRobot(robot)<=5
                    && getCostEForRobot(robot)<=5 && getTotolCostForRobot(robot)<=10;
            return staticValid;
        }

        //动态评估是否可以
        boolean isEnvValid(int step){
            boolean result = true;
            int a = robot.getRequiredCount(Type.A,this);
            int b= robot.getRequiredCount(Type.B,this);
            int c = robot.getRequiredCount(Type.C,this);
            int d = robot.getRequiredCount(Type.D,this);
            int e = robot.getRequiredCount(Type.E,this);
            //如果不够了
            if(a+b+c+d+e + robot.getMoleculeCount()>10){
                return false;
            }
            if(a>0){
                result = result && env.a+ enemyGoingToRelease(Type.A,step)>=a;
            }
            if(b>0){
                result = result && env.b+ enemyGoingToRelease(Type.B,step)>=b;
            }
            if(c>0){
                System.err.println("env.c="+env.c+",required C="+c);
                result = result && env.c+ enemyGoingToRelease(Type.C,step)>=c;
            }
            if(d>0){
                result = result && env.d+ enemyGoingToRelease(Type.D,step)>=d;
            }
            if(e>0){
                result = result && env.e+ enemyGoingToRelease(Type.E,step)>=e;
            }
            return result;
        }

        @Override
        public int compareTo(Sample o) {
            boolean canContributeToChosenProject = false;
            //敌人的按分数高低排序
            if(getCarriedBy() == 1 && o.getCarriedBy() == 1){
                return o.health - health;
            }
            //前期努力冲project
//            if(getLevel()==1){
//                int r = getProjectRequiredScore(o.expertiseGain) - getProjectRequiredScore(expertiseGain);
//                if(r==0){
//                    return o.health - health;
//                }
//                else{
//                    return r;
//                }
//            }
            Project chosenProject = getChosenProject(robot,enemy);
            //如果需求最少的project只需要2个以下
            if(chosenProject!=null){
//                System.err.println("需求最少的project还需要"+chosenProject.getTotalRequired(robot)+"个type");
            }
            if(chosenProject!=null){
                //分数大的排前面
                for(Sample sample:robot.getSamples().values()){
                    //至少有一个持有的expertise是chosenProject需要的
                    if(getChosenProjectRequiredScore(chosenProject,sample.expertiseGain)>0){
                        canContributeToChosenProject = true;
                        break;
                    }
                }

            }

            //中期尝试冲最有希望的project
            if(canContributeToChosenProject){
                //如果需求少于3个或者分数相等，看需求排序
                if((chosenProject.getTotalRequired(robot)<4 && round<=140)
                        || (chosenProject.getTotalRequired(robot)<2 && round<=180)
                        ||getLevel()==1
                        || o.health == health){
//                    System.err.println("排序策略是project优先");
                    int result = getChosenProjectRequiredScore(chosenProject,o.expertiseGain) - getChosenProjectRequiredScore(chosenProject,expertiseGain);
                    if(result == 0){
                        return o.health - health;
                    }
                    return result;
                }
            }
            //默认按分数排序
            return o.health - health;
        }
    }

    private enum State {
        diagnosed,undiagnosed;
    }

    enum Type{
        A("A"),B("B"),C("C"),D("D"),E("E");
        private String value;

        static Type[] types={A,B,C,D,E};

        Type(String value){
            this.value = value;
        }

        String value(){
            return value;
        }
    }
    private static class Robot {

        int countA=0,countB=0,countC=0,countD=0,countE=0;
        Map<Integer,Sample> samples = new HashMap<>(3);

        int expertiseA, expertiseB,expertiseC,expertiseD,expertiseE;
        String location;
        int eta,score;

        int getCountByType(String type){
            switch (type){
                case "A":return countA;
                case "B":return countB;
                case "C":return countC;
                case "D":return countD;
                case "E":return countE;
            }
            return 0;
        }

        int getExpertiseByType(String type){
            switch (type){
                case "A":return expertiseA;
                case "B":return expertiseB;
                case "C":return expertiseC;
                case "D":return expertiseD;
                case "E":return expertiseE;
            }
            return 0;
        }

        int getReadySampleNumber(){
            int number =0;
            for(Sample sample:samples.values()){
                if(getRequiredCount(sample)==0){
                    number ++;
                }
            }
            return number;
        }

        public String getLocation() {
            return location;
        }

        public int getEta() {
            return eta;
        }

        int getExpertise(){
            return expertiseA+expertiseB+expertiseC+expertiseD+expertiseE;
        }

        int countLevel(int rank){
            int count =0;
            for(Sample sample:samples.values()){
                if(sample.getRank() == rank){
                    count ++;
                }
            }
            return count;
        }


        /*
        减库存，发命令
         */
        void produceOnLab(int sampleId){
//            if(command.isEmpty()){
//                Sample sample =  samples.get(sampleId);
//                int a = sample.getCostAForRobot();
//                int b = sample.getCostBForRobot();
//                int c = sample.getCostCForRobot();
//                int d = sample.getCostDForRobot();
//                int e = sample.getCostEForRobot();
//                if(a>0 && countA >=a){
//                    countA -=a;
//                }
//                if(b>0 && countB >=b){
//                    countB -=b;
//                }
//                if(c>0 && countC >=c){
//                    countC -=c;
//                }
//                if(d>0 && countD >=d){
//                    countD -=d;
//                }
//                if(e>0 && countE >=e){
//                    countE -=e;
//                }
//            }
            setCommand("CONNECT " + sampleId);
        }

        void putToCloud(int sampleId){
//            if(command.isEmpty()){
//                samples.remove(sampleId);
//            }
            setCommand("CONNECT " + sampleId);
        }

        void takeFromCloud(Sample sample){
//            if(command.isEmpty()){
//                samples.remove(sample.getSampleId());
//            }
            setCommand("CONNECT " + sample.getSampleId());
        }

        void diagnosSample(Sample sample){
//            if(command.isEmpty()) {
//                samples.put(sample.getSampleId(),sample);
//            }
            setCommand("CONNECT " + sample.getSampleId());
        }

        void takeMolecule(Type type){
            if(robot.getMoleculeCount()>=10){
                return;
            }
            setCommand("CONNECT " + type.value());
        }

        int getMoleculeCount(){
            return countA+countB+countC+countD+countE;
        }

        boolean envTypesReadyForSample(Sample sample){
            for(Type type:Type.types){
                if(env.getAvailableCountByType(type)<getRequiredCount(type,sample)){
                    return false;
                }
            }
            return true;
        }

        boolean envTypesReadyForSamples(Sample ...  samples){
            Set<Integer> sampleSet = new HashSet<>();
            for(Sample s : samples){
                sampleSet.add(s.getSampleId());
            }
            for(Type type:Type.types){
                if(env.getAvailableCountByType(type)<getRequiredCount(type,sampleSet)){
                    return false;
                }
            }
            return true;
        }


        public Map<Integer, Sample> getSamples() {
            return samples;
        }

        public int getTotalHealth(){
            int score = 0;
            for(Sample sample:samples.values()){
                score+=sample.getHealth();
            }
            return score;
        }

        public void setSamples(Map<Integer, Sample> samples) {
            this.samples = samples;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            return sb.toString();
        }

        public int getRequiredCount(Type type, Sample sample){
            int count = 0;
            switch (type){
                case A:
                    count += sample.getCostAForRobot(this);
                    count -= countA;
                    break;
                case B:
                    count += sample.getCostBForRobot(this);
                    count -= countB;
                    break;
                case C:
                    count += sample.getCostCForRobot(this);
                    count -= countC;
                    break;
                case D:
                    count += sample.getCostDForRobot(this);
                    count -= countD;
                    break;
                case E:
                    count += sample.getCostEForRobot(this);
                    count -= countE;
                    break;
            }
            return Math.max(0,count);
        }

        public int getRequiredCount(Type type, int sampleId){
            return getRequiredCount(type,samples.get(sampleId));
        }

        public int getRequiredCount(int sampleId){
            int count = 0;
            for(Type type:Type.types){
                count += getRequiredCount(type,sampleId);
            }
            return count;
        }

        public int getRequiredCount(Sample sample){
            int count = 0;
            for(Type type:Type.types){
                count += getRequiredCount(type,sample);
            }
            return count;
        }


        public Set<Type> getRequiredTypes(Set<Integer> readySamples){
            Set<Type> typeSet = new HashSet<>();
            for(int id: readySamples){
                if(getRequiredCount(Type.A,id)>0){
                    typeSet.add(Type.A);
                }
                if(getRequiredCount(Type.B,id)>0){
                    typeSet.add(Type.B);
                }
                if(getRequiredCount(Type.C,id)>0){
                    typeSet.add(Type.C);
                }
                if(getRequiredCount(Type.D,id)>0){
                    typeSet.add(Type.D);
                }
                if(getRequiredCount(Type.E,id)>0){
                    typeSet.add(Type.E);
                }
            }
            return typeSet;
        }

        public int getRequiredCount(Set<Integer> readySamples){
            int count = 0;
            for(Type type:Type.types){
                count +=getRequiredCount(type,readySamples);
            }
            return count;
        }

        /*
        找出readySamples需要的type个数
         */
        public int getRequiredCount(Type type, Set<Integer> readySamples){
            int count = 0;
            switch (type){
                case A:
                    for(int id: readySamples){
                        count +=samples.get(id).getCostAForRobot(this);
                    }
                    count -= countA;
                    break;
                case B:
                    for(int id: readySamples){
                        count +=samples.get(id).getCostBForRobot(this);
                    }
                    count -= countB;
                    break;
                case C:
                    for(int id: readySamples){
                        count +=samples.get(id).getCostCForRobot(this);
                    }
                    count -= countC;
                    break;
                case D:
                    for(int id: readySamples){
                        count +=samples.get(id).getCostDForRobot(this);
                    }
                    count -= countD;
                    break;
                case E:
                    for(int id: readySamples){
                        count +=samples.get(id).getCostEForRobot(this);
                    }
                    count -= countE;
                    break;
            }
            return Math.max(count,0);
        }

    }

    //    public static int getLevel() {
//        //小于4
//        if(robot.getExpertise()<4){
//            return 1;
//        }
//        if(robot.getMoleculeCount() == 10 && robot.getReadySampleNumber() == 0){
//            return 1;
//        }
//        //4-6
//        else if(robot.getExpertise()<=6){
//            if(robot.countLevel(2)>1){
//                return 1;
//            }
//            return 2;
//        }
//        //7-9
//        else if(robot.getExpertise()<=9){
//            if(robot.countLevel(3)>=1) {
//                return 2;
//            }
//            else {
//                return 3;
//            }
//        }
//        //9-11
//        else if(robot.getExpertise()<=11){
//            if(robot.countLevel(3)>0){
//                return 2;
//            }
//            else {
//                return 3;
//            }
//        }
//        else{
//            if(robot.countLevel(3)>1){
//                return 2;
//            }
//            else {
//                return 3;
//            }
//        }
//    }
    /*
    TODO:调整参数
     */
    static int getLevel() {
        //0-7 全拿1
        if(robot.getExpertise()<=7){
            return 1;
        }
        if(robot.getMoleculeCount() == 10 && robot.getReadySampleNumber() == 0){
            return 1;
        }
        if(robot.getExpertise()<=9){
            if(robot.countLevel(2)>1){
                return 1;
            }
            else {
                return 2;
            }
        }
        if(robot.getExpertise()<=11){
            return 2;
        }
        if(robot.getExpertise()<=13){
            if(robot.countLevel(3)>0){
                return 2;
            }
            else {
                return 3;
            }
        }
        if(robot.getExpertise()<=15){
            if(robot.countLevel(3)>1){
                return 2;
            }
            else {
                return 3;
            }
        }
        return 3;
    }

    static class Env{
        int a,b,c,d,e;

        int getAvailableCountByType(Type type){
            switch (type){
                case A:return a;
                case B:return b;
                case C:return c;
                case D:return d;
                case E:return e;
                default:return 0;
            }
        }

        int getAll(){
            return a+b+c+d+e;
        }

        int getMinAvailable(){
            int min = Math.min(a,b);
            min = Math.min(min,c);
            min = Math.min(min,d);
            min = Math.min(min,e);
            return min;
        }
    }

    /*
    下一步敌人可能要拿起或者放下
     */
    private static int enemyGoingToRelease(Type type, int step){
        int count = 0;
        if(enemy.location.equals(LABORATORY)){
            for(Sample sample:enemy.getSamples().values()){
                //ready one
                if(enemy.getRequiredCount(sample)<=0){
//                    System.err.println("enemy has ready sample:" + sample.getSampleId()+", going to release type"+type.value+" with number " + sample.getCostForRobot(type.value,enemy));
                    count+=sample.getCostForRobot(type.value,enemy);
                }
            }
            System.err.println("敌人将要释放"+count+"个"+type.value);
            count = Math.min(enemy.getCountByType(type.value),count);
        }
        /*
        如果敌人在材料台，他随后可能会释放多少个type X.
         */
        else if(enemy.location.equals(MOLECULES)&&enemy.eta < step){
            //如果enemy持有这个type
            if(enemy.getCountByType(type.value)>0){
                for(Sample sample:enemy.getSamples().values()){
                    //如果当前sample对于enemy需要至少1个type,则意味释放时会释放type A
                    if(sample.getCostForRobot(type.value,enemy)>0){
                        //check sample是否拿得完
                        boolean canDo = true;
                        for(Type t:Type.types){
                            if(env.getAvailableCountByType(t)<enemy.getRequiredCount(t,sample)){
                                canDo = false;
                            }
                        }
                        //如果sample可以ready,则预判后面敌人会释放sample需要的type
                        if(canDo){
                            /*
                            TODO:只算了第一个可能ready的sample释放的type
                             */
                            count += sample.getCostForRobot(type.value,enemy);
                            break;
                        }
                    }
                }
            }
        }
        System.err.println("敌人可能会释放"+count+"个type"+type.value);
        return count;
    }

    static List<Sample> enemyPotentialReadySamples(){
        List<Sample> readySamples = new ArrayList<>();
        for(Sample sample: enemy.getSamples().values()){
            if(sample.getState() == State.diagnosed){
                if(enemy.envTypesReadyForSample(sample)){
                    readySamples.add(sample);
                }
            }
        }
        readySamples.sort(Sample::compareTo);
        return readySamples;
    }

    static List<Sample> enemyLeadSuprPassSamples(){
        int gap = robot.score - enemy.score;
        List<Sample> blackList = new ArrayList<>();
        List<Sample> readySamples = enemyPotentialReadySamples();
        if(gap<0){
            return blackList;
        }

        System.err.println("敌人潜在的readySamples"+readySamples);

        for(Sample sample:readySamples){
            int gain = sample.health;
            if(canFinishProject(enemy, robot,sample)){
                gain +=50;
            }
            if(gap<gain){
                blackList.add(sample);
            }
        }
        if(!blackList.isEmpty()){
            return blackList;
        }

        if(readySamples.size() == 2){
            Sample s1 = readySamples.get(0);
            Sample s2 = readySamples.get(1);
            tryToFillBlackList(gap, blackList, s1,s2);
            if(!blackList.isEmpty()){
                blackList.sort(Sample::compareTo);
                return blackList;
            }
        }
        else if(readySamples.size() == 3){
            Sample s1 = readySamples.get(0);
            Sample s2 = readySamples.get(1);
            Sample s3 = readySamples.get(2);
            tryToFillBlackList(gap, blackList, s1,s2);
            tryToFillBlackList(gap, blackList, s1,s3);
            tryToFillBlackList(gap, blackList, s2,s3);
            System.err.println("黑名单为"+blackList);
        }
        blackList.sort(Sample::compareTo);
        return blackList;
    }

    private static void tryToFillBlackList(int gap, List<Sample> blackList, Sample s1, Sample s2) {
        System.err.println("env里的对于"+s1.sampleId+","+s2.sampleId+"够不够?"+enemy.envTypesReadyForSamples(s1,s2));
        if(!enemy.envTypesReadyForSamples(s1,s2)){
            return;
        }
        int gain = s1.health + s2.health;
        if(canFinishProject(enemy,robot,s1,s2)){
            gain +=50;
        }
        if(gap<gain){
            blackList.add(s1);
            blackList.add(s2);
            return;
        }
    }

    static boolean canFinishProject(Robot robot, Robot enemy, Sample ... samples){
        Project.enemyUse=true;
        Project project = getChosenProject(robot, enemy);
        List<Type> required = new ArrayList<>();

        if(project == null){
            return false;
        }
        if(project.getTotalRequired(robot)>0 && project.getTotalRequired(robot)<=samples.length){
            for(Type type:Type.types){
                if(project.getRequiredNumber(robot,type.value)==1){
                    required.add(type);
                    break;
                }
                else if(project.getRequiredNumber(robot,type.value)==2){
                    required.add(type);
                    required.add(type);
                    break;
                }
            }
            System.err.println("完成project需要的types为:"+required);
            int len = required.size();
            for(Type t:required){
                for(Sample s:samples){
                    if(t.value.equals(s.getExpertiseGain())){
                        len--;
                    }
                }
            }
            return len ==0;
        }
        return false;
    }
}