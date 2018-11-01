import java.util.*;

class Player {

    private static final String SAMPLES = "SAMPLES", DIAGNOSIS = "DIAGNOSIS", MOLECULES = "MOLECULES", LABORATORY = "LABORATORY";

    private static Robot robot = new Robot();
    private static Robot enemy = new Robot();
    private static String command = "";
    private static int sCount = 0;
    private static Env env = new Env();
    static final int THRESHOLD = 181;
    static Project[] projects = new Project[3];
    //从材料台取不够材料
    static boolean giveUp = false;
    //最少从分析台拿够2个才走
    static int minCarryNumber =2;
    static Type defendType;

    //是否开启最终防守模式
    static boolean enableFinalDefend = false;
    static boolean enableProjectDefend = false;
    static Map<Integer,Sample> sampleInCloud;

    private static void setCommand(String cmd){
        if(command.isEmpty()){
            System.err.println("setCommand: " + cmd);
            command = cmd;
        }
    }

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
        score =  required>0?1:0;
        return score;
    }
    static boolean projectAvailable(){
        for(int i=0;i<projects.length;i++){
            int required = projects[i].getTotalRequired(robot);
            int erequired = projects[i].getTotalRequired(enemy);
            if(required>0 && erequired>0){
                return true;
            }
        }
        return false;
    }

    private static List<Project> getChosenProjects(Robot r, Robot e){
        Arrays.sort(projects);
        List<Project> chosenProjects = new ArrayList<>();
        for(int i=0;i<projects.length;i++){
            //0<需求<=3
            //敌人未完成
            int required = projects[i].getTotalRequired(r);
            if(required>0 && required<=3 && projects[i].getTotalRequired(e)>0){
                chosenProjects.add(projects[i]);
            }
        }
        return chosenProjects;
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
            previousEnemyMoleculeCount = enemy.getMoleculeCount();
            previousRoundEnemyEta = enemy.eta;
            previousRoundEnemyLocation = enemy.location;
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
            System.err.print("round="+round+",robot在"+robot.getLocation()+",距离为"+robot.eta);
            System.err.println(",enemy在"+enemy.getLocation()+",距离为"+enemy.eta);
            System.err.println("robot持有"+robot.getMoleculeCount()+"个molecule,"+robot.getSamples().size()+"个sample");
            sampleList.sort(Sample::compareTo);
            Map<String,Set<Sample>> lll = sampleLeadEnemyFinishProject();
            if(hasProjectDefendCondition(robot.location,lll)){
                enableProjectDefend = true;Project.enemyUse=true;
                System.err.println("开启project防御:"+lll);
            }
            else{
                enableProjectDefend = false;Project.enemyUse = false;
            }
            System.err.println("project防御?"+ enableProjectDefend+"..."+lll);
            if(round>=THRESHOLD){
                Map<String,Set<Sample>> defendList = enemyLeadSuprPassSamples();
                if(hasFinalDefendCondition(robot.location,defendList)){
                    enableFinalDefend = true;Project.enemyUse=true;
                    System.err.println("开启final防御:"+defendList);
                }
                else{
                    enableFinalDefend = false;Project.enemyUse = false;
                }
                System.err.println("final防御?"+ enableFinalDefend+"..."+defendList);
            }
            /*
            看哪个type可以防御
             */
            defendType = null;int maxRequired=0;
            for(Type type:Type.types){
                if(enemy.getExpertiseByType(type.value) ==0 && enemy.getCountByType(type.value) + enemy.getExpertiseByType(type.value)<=1
                        && env.getAvailableCountByType(type)+robot.getMoleculeCount()<=8){
                    if(defendType==null){defendType = type;}
                    int required = enemy.getRequiredCount(type,enemy.samples.keySet());
                    if(required>maxRequired){
                        defendType = type;
                        maxRequired = required;
                    }
                }
            }
            // if(projectAvailable() &&robot.crazyModeRank()>0){
            //     enableCrazeMode=true;
            // }
            // else if(robot.location.equals(SAMPLES) && !robot.hasUnDiggnosicSample()){
            //     enableCrazeMode = false;
            // }
            machine.before();
            for(Sample sample: sampleList){
                System.err.println(sample);
                machine.execute(sample);
            }
            machine.executeFinal();
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
                    if(enemy.score -robot.score<30){
                        level = level == 3?2:level;
                    }
                }
                else if(level==3 && robot.getPotentialReadySamplesWithHelp().size()==1){
                    level = 2;
                }
                System.err.println("最后一次拿"+level);
            }
            if(enableFinalDefend || enableProjectDefend){
                setCommand("GOTO " + MOLECULES);
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
                    //有至少一个未分析的
                    if(robot.hasUnDiggnosicSample() && round >= 185){setCommand("GOTO " + DIAGNOSIS);}
                    else{setCommand("CONNECT " + level);}
                }
                else{
                    if(robotCanTakeSampleNumberInCloud(level-1)>= 3- robot.getSamples().size()){
                        setCommand("GOTO " + DIAGNOSIS);
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

    private static class DiggnosisMachine implements Machine{
        @Override
        public void before() {
            if(robot.eta>0){
                setCommand("GOTO " + DIAGNOSIS);
                return;
            }

            if(enableProjectDefend){
                setCommand("GOTO " + MOLECULES);
            }

            if(enableFinalDefend){
                if(!robot.hasUnDiggnosicSample()){
                    setCommand("GOTO " + MOLECULES);
                }
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
                else if(robot.getMoleculeCount()==10&&robot.getPotentialReadySamplesWithHelp().size() == 0){
                    if(robot.getRequiredCount(sample)>0){
                        robot.putToCloud(sample.getSampleId());
                    }
                }
                //已分析的
                else{
                    if(!sample.isStaticValid()){
                        robot.putToCloud(sample.getSampleId());
                    }
                    //sample不合格,或者不符合冲刺要求
                    else if(!sample.isEnvValid(3)){
                        System.err.println("sample"+sample.sampleId+"不合格");
                        //如果当前sample不能完成project  或者可以ready的 sample较少
                        if(!canFinishProject(robot,enemy,sample) ||robot.getPotentialReadySamplesWithHelp().size() <minCarryNumber ){
                            if(enemy.getRequiredCountCorrelation(sample)>0){
                                robot.putToCloud(sample.getSampleId());
                            }
                            if(robot.samples.size() == 3){
                                robot.putToCloud(sample.getSampleId());
                            }
                        }
                    }
                }
            }
            else if(sample.carriedBy == -1){
                if(robot.samples.size() ==3){
                    return;
                }
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
                        if(robot.getRequiredCountCorrelation(sample)==0){
                            robot.takeFromCloud(sample);
                        }
                        return;
                    }
                    robot.takeFromCloud(sample);
                }
            }
        }

        @Override
        public void executeFinal() {
              /*
            如果资源有限，拿不起
            TODO:目前是cloud里至少有2个拿不起，而且最少的type小于2个
             */
            if((sampleInCloud.size()>=2 && (env.getMinAvailable(robot)< 2)) || round >= THRESHOLD){
                minCarryNumber = 1;
            }
            if(round>=194 &&robot.getReadySampleNumber()>=minCarryNumber){
                setCommand("GOTO " + LABORATORY);
            }
            if(robot.getSamples().size()>0 && robot.getRequiredCount(robot.samples.keySet())==0){
                if(robot.samples.size()>=minCarryNumber){
                    setCommand("GOTO " + LABORATORY);
                }
                else{setCommand("GOTO " + SAMPLES);}
            }
            if(robot.getSamples().size()>= minCarryNumber){
                if(robot.getReadySampleNumber()>0){
                    for(Sample s:robot.samples.values()){
                        if(!robot.envTypesReadyForSampleWithHelp(s)){
                            setCommand("GOTO " + LABORATORY);
                        }
                    }
                    setCommand("GOTO " + MOLECULES);
                }
                else{
                    boolean hasValid=false;int dropId=-1;
                    for(Sample s:robot.samples.values()){
                        if(s.isEnvValid(3)){
                            hasValid = true;break;
                        }
                        else{
                            dropId = s.sampleId;
                        }
                    }
                    if(hasValid){
                        setCommand("GOTO " + MOLECULES);
                    }
                    else if(dropId>=0){
                        setCommand("CONNECT " + dropId);
                    }
                    System.err.println("hasValid="+hasValid+",dropId="+dropId);
                }

            }
            else{setCommand("GOTO " + SAMPLES);}
        }
    }

    private static class MoleculeMachine implements Machine{


        @Override
        public void before() {
            if(robot.eta>0){
                setCommand("GOTO " + MOLECULES);
                return;
            }
            if(round==200-3 && robot.getReadySampleNumber()>0){
                command = "GOTO "+ LABORATORY;
            }
            if(enableFinalDefend || enableProjectDefend){
                String defend = "";
                Map<String,Set<Sample>> sMap;
                if(enableFinalDefend){
                    sMap = enemyLeadSuprPassSamples();
                }
                else{
                    sMap = sampleLeadEnemyFinishProject();
                }
                System.err.println("yyyy " + sMap);
                Set<Sample> samples = null;
                if(sMap.get("all")!=null){
                    samples = sMap.get("all");
                    if(samples.size() == 1){
                        defend = "all";
                    }
                }
                else if(sMap.get("either")!=null){
                    samples = sMap.get("either");
                    defend = "either";
                }
                if(!defend.isEmpty() && samples != null && !samples.isEmpty()){
                    System.err.println("xxxx " + sMap);
                    tryDefend(new ArrayList<>(samples),defend);
                }
            }
        }

        @Override
        public void execute(Sample sample) {
            if(sample.getCarriedBy()==0){
                if(sample.getState() == State.undiagnosed){
                    return;
                }
                //如果自己拿不够，先跳过
                if(robot.getRequiredCountCorrelation(sample) + robot.getMoleculeCount()>10){
                    return;
                }
                Type chosenType = null;
                //自己是ready了
                for(Type type: Type.types){
                    //对于某个type需要的数量
                    ////需要拿而且有
                    int iRequire = robot.getRequiredCountConsiderCombinition(type,sample);
                    int eAvail = env.getAvailableCountByType(type);
                    if(iRequire>0 && eAvail>0 && eAvail>=iRequire){
                        if(chosenType==null){chosenType = type;}
                        if(eAvail==1){chosenType = type;break;}
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
            boolean hasReady = false;
            Set<Integer> readySamples = new HashSet<>();
            Set<Integer> noReadySamples = new HashSet<>();
            for(Sample s:robot.getSamples().values()){
                if(s.getState() == State.undiagnosed){
                    continue;
                }
                if(robot.getRequiredCountCorrelation(s) ==0){
                    readySamples.add(s.sampleId);
                    hasReady = true;
                }
                else{
                    noReadySamples.add(s.sampleId);
                }
            }
            System.err.println("round="+round+",readySamples="+readySamples+",hasReady="+hasReady);
            //所有ready Samples都ready
            int readySampleRequiedCount = robot.getRequiredCount(readySamples);
            boolean allReadySamplesDone = readySampleRequiedCount == 0;
            System.err.println("has readySample:"+hasReady+",readySamples:" + readySamples+",allReadySamplesDone="+allReadySamplesDone);
            //如果有至少1个 sample ready了
            if(hasReady){
                //TODO:先尝试防守
                tryDefend(enemy.getPotentialReadySamplesWithHelp(),"all");
            }
            if(!command.isEmpty()){
                return;
            }
            //Ready Samples:  任意一个ready了
            //优先拿readySamples需要的
            if(readySampleRequiedCount + robot.getMoleculeCount() <=10){
                for(Type type:Type.types){
                    //某个type没有了
                    if(env.getAvailableCountByType(type)<=0){
                        continue;
                    }
                    int tRequired = robot.getRequiredCount(type,readySamples);
                    if(tRequired>0 &&env.getAvailableCountByType(type)>=tRequired){
                        robot.takeMolecule(type);
                        break;
                    }
                }
            }

            /*
            如果ready sample拿不满了
            要么满10个了
            要么资源不够了
             */
            if(hasReady && !allReadySamplesDone){
                for(Sample rSample :robot.getPotentialReadySamplesWithHelp()){
                    if(robot.getRequiredCountCorrelation(rSample) == 0){
                        continue;
                    }
                    for(Type rType:Type.types){
                        int rCount = robot.getRequiredCountCorrelation(rType,rSample);
                        if(rCount>0 && rCount<= enemyGoingToRelease(rType,0) && canWait() && enemy.location.equals(LABORATORY)){
                            setCommand("WAIT");
                        }
                    }
                }
                Set<Type> noReadySamplesRequiredTypes = robot.getRequiredTypes(noReadySamples);

                for(int id:noReadySamples){
                    Sample noReadySample = robot.samples.get(id);
                    if(robot.envTypesReadyForSampleWithHelp(noReadySample)){
                        for(Type type:noReadySamplesRequiredTypes){
                            if(robot.getRequiredCountCorrelation(type,noReadySample)>0 && env.getAvailableCountByType(type)>0){
                                System.err.println("Sample"+id+",type"+type.value()+"需要"+robot.getRequiredCountCorrelation(type,noReadySample)+"个");
                                robot.takeMolecule(type);
                                break;
                            }
                        }
                    }
                }
                setCommand("GOTO " + LABORATORY);
            }

            /*
            ready sample拿满了，该继续进攻还是防守?
             */
            else{
                //尝试拿没ready的
                Set<Type> noReadySamplesRequiredTypes = robot.getRequiredTypes(noReadySamples);
                for(int id:noReadySamples){
                    Sample noReadySample = robot.samples.get(id);
                    for(Type type:noReadySamplesRequiredTypes){
                        if(robot.getRequiredCountCorrelation(type,noReadySample)>0 && env.getAvailableCountByType(type)>0){
                            System.err.println("Sample"+id+",type"+type.value()+"需要"+robot.getRequiredCountCorrelation(type,noReadySample)+"个");
                            robot.takeMolecule(type);
                            break;
                        }
                    }
                }
                //都试过了，有sample ready的话就去LAB吧
                if(hasReady){
                    if(defendType!=null && env.getAvailableCountByType(defendType)>0 && robot.getMoleculeCount()<10){
                        System.err.println("防御"+defendType.value());
                        robot.takeMolecule(defendType);
                    }
                    setCommand("GOTO " + LABORATORY);
                }
                //悲剧，都试过了，还没有sample ready,OMG
                else{
                    if(defendType!=null){
                        robot.takeMolecule(defendType);
                    }
                    //拿不动了,回去放
                    if(robot.getMoleculeCount() == 10){
                        if(command.isEmpty()){
                            giveUp = true;
                        }
                        if(robot.samples.size()>=2){
                            setCommand("GOTO " + DIAGNOSIS);
                        }
                        else{
                            setCommand("GOTO " + SAMPLES);
                        }
                    }
                    //TODO:待优化
                    //敌人卡死，则自己比较傻
                    //敌人如果判断自己分多,可以死等
                    //看能不能等
                    else{
                        boolean canWait = false;
                        for(Sample sample:robot.getSamples().values()){
                            if(sample.isStaticValid() && sample.isEnvValid(0)){
                                canWait = true;
                                break;
                            }
                        }
                        //可以等待的条件
                        //如果上一轮敌人手上readyCount 和这一轮相等,则证明他在等待防守
                        if(canWait && canWait()){
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

        private void tryDefend(List<Sample> defendSamples, String defendType) {
            if(enemy.getLocation().equals(DIAGNOSIS) || enemy.getLocation().equals(MOLECULES)){
                //敌人可以ready的samples,按照分数高低排序,优先破坏他分数高的
                int minDestroyRequired = 999; Type destroyType = null;
                here:
                for(Sample sample:defendSamples){
                    boolean canReady = enemy.envTypesReadyForSampleWithHelp(sample);
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
                        if(required<=0){
                            continue;
                        }
                        int helpCount = 0;

                        helpCount +=  helpTypeNumber(enemy,sample,tt);
                        required -= helpCount;
                        System.err.println("对于sample"+sample.sampleId+",敌人还需要"+required+"个"+tt.value);
                        int destryRequired = env.getAvailableCountByType(tt) - required + 1;
                        int enemyNeedStep;
                        if(enemy.location.equals(MOLECULES)){
                            enemyNeedStep = enemy.eta + required;
                        }
                        else{
                            enemyNeedStep = 3 + required;
                        }
                        System.err.println("防守需要"+destryRequired+"个"+tt.value+",敌人需要"+enemyNeedStep+"步");
                        //开始破坏
                        if(space >= destryRequired && enemy.getMoleculeCount()<10 && env.getAvailableCountByType(tt)>0){
                            if(destryRequired<enemyNeedStep){
                                System.err.println("开始防守,敌人的sample"+sample.getSampleId()+",type="+tt.value());
                                robot.takeMolecule(tt);
                                System.err.println(command);
                                break here;
                            }
                            else if(defendType.equals("either")){
                                if(minDestroyRequired>destryRequired){
                                    minDestroyRequired = destryRequired;
                                    destroyType = tt;
                                }
                            }
                        }
                    }
                }
                if(destroyType!=null){
                    robot.takeMolecule(destroyType);
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
                if(sample.getState() == State.undiagnosed){
                    return;
                }
                if(robot.getRequiredCount(sample.getSampleId()) ==0){
                    /*
                    当敌人手上有未ready sample且在等待时触发防守:
                    先判断释放了会不会救了敌人
                    如果会就WAIT
                     */
                    if(enemy.getLocation().equals(MOLECULES)
                            && enemy.getSamples().size() > enemy.getReadySampleNumber()){
                        for(Sample sampleForEnemy: enemy.getSamples().values()){
                            if(enemy.getRequiredCount(sampleForEnemy) >0){
                                for(Type type:Type.types){
                                    //如果敌人需求大于存量,敌人可能在等我释放
                                    if(enemy.getRequiredCount(type,sampleForEnemy)> env.getAvailableCountByType(type)){
                                        System.err.println("敌人对于"+sampleForEnemy.sampleId+"需求"+type.value+" " + enemy.getRequiredCount(type,sampleForEnemy)+"个"+",本人即将释放"+sample.getCostForRobot(type.value,robot)+"个");
                                        //如果将要释放的type>=敌人需要的
                                        if(sample.getCostForRobot(type.value,robot)>= enemy.getRequiredCount(type,sampleForEnemy)-env.getAvailableCountByType(type)){
                                            //如果敌人只需要这个type了
                                            if(enemy.getRequiredCount(sampleForEnemy) == enemy.getRequiredCount(type,sampleForEnemy)){
                                                //如果自己分多
                                                int gap = robot.score - enemy.score;
                                                if(robot.getReadySampleNumber()>0){
                                                    //敌人在等待释放或者敌人手上只有未ready sample
                                                    if(enemyWaitForRelease()|| (enemy.samples.size()>0 &&enemy.getReadySampleNumber()==0)){
                                                        if(gap>0 || (round>THRESHOLD && gap+sample.health>0)){
                                                            if(round<200){
                                                                command = "PENDING";
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                    //如果敌人手上所有的sample都差即将释放的type而且已经空了
                    if(enemy.getReadySampleNumber()==0){
                        for(Type t:Type.types){
                            if(env.getAvailableCountByType(t)==0){
                                boolean allRequire = true;
                                for(Sample eSample:enemy.samples.values()){
                                    if(eSample.getCostForRobot(t.value,enemy)==0){
                                        allRequire = false;
                                        break;
                                    }
                                }
                                //即将释放的type是敌人手中所有sample都需要的
                                if(allRequire && robot.getRequiredCount(t,sample)>0){
                                    int gap = robot.score - enemy.score;
                                    if(gap>=0 || (round>THRESHOLD && gap+sample.health>0)){
                                        if(round<200){
                                            command = "PENDING";
                                            break;
                                        }
                                    }
                                }
                            }
                        }
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
            if(command.equals("PENDING")){
                command = "WAIT";
            }
            if(!command.isEmpty()){
                return;
            }

            if(enableFinalDefend || enableProjectDefend){
                setCommand("GOTO " + MOLECULES);
            }

            if(defendType!=null && env.getAvailableCountByType(defendType)>0 && robot.getMoleculeCount()+env.getAvailableCountByType(defendType)<=8){
                if(enemy.location.equals(MOLECULES)){
                    //敌人在,不防
                }
                else{
                    setCommand("GOTO " + MOLECULES);
                }

            }

            /*
            如果手头少于一个sample而且分数小于30
            或者最后几轮手里没sample了
             */
            if(round<THRESHOLD && robot.getSamples().size()<=1 && robot.getTotalHealth()<30){
                //如何敌人不会竞争cloud里的sample
                int robotCanTakeSampleNumberInCloud = robotCanTakeSampleNumberInCloud(getLevel()-1);
                System.err.println("可以从cloud里拿"+robotCanTakeSampleNumberInCloud+"个");
                if(robotCanTakeSampleNumberInCloud >0
                        && robotCanTakeSampleNumberInCloud >= 3 - robot.getSamples().size()
                        && robotCanTakeSampleNumberInCloud >= minCarryNumber){
                    setCommand("GOTO " + DIAGNOSIS);
                }
                else {
                    setCommand("GOTO " + SAMPLES);
                }
            }
            //手上的sample大于30分或者最后几轮了且手上有至少一个sample
            else{
                boolean canDo = false;
                Type enemyCandefendType = null;int iRequired=0;
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
                        for(Type type:Type.types){
                            if(robot.getRequiredCount(type,sample)==robot.getRequiredCount(sample)){
                                enemyCandefendType = type;
                                iRequired = robot.getRequiredCount(type,sample);
                                break;
                            }
                        }
                        canDo=true;
                        break;
                    }
                }
                if(enemyCandefendType!=null && iRequired>0){
                    if(enemy.location.equals(MOLECULES)){
                        int eRequired = env.getAvailableCountByType(enemyCandefendType)-iRequired + 1 + enemy.eta;
                        if(eRequired< iRequired+3){
                            canDo = false;
                        }
                    }
                }
                //如果MOLECULES的type可能足够
                if(canDo){
                    if(round>170 && robot.getSamples().size()==1){
                        Sample sample = new ArrayList<>(robot.getSamples().values()).get(0);
                        int score = sample.health;
                        if(canFinishProject(robot,enemy,sample)){
                            score += 50;
                        }
                        if(score > enemy.score - robot.score){
                            setCommand("GOTO " + MOLECULES);
                        }
                    }
                    else{
                        setCommand("GOTO " + MOLECULES);
                    }

                }
                //优先尝试去DIAGNOSIS取
                if(robotCanTakeSampleNumberInCloud(getLevel()-1)>=1){
                    setCommand("GOTO " + DIAGNOSIS);
                }
                else{
                    setCommand("GOTO " + SAMPLES);
                }
            }
        }
    }

    static int robotCanTakeSampleNumberInCloud(int minRank){
        int count =0;
        for(Sample sample:sampleInCloud.values()){
            if(sample.rank>=minRank && sample.isStaticValid() && sample.isEnvValid(4)){
                if(enemyCanTakeFromCloud(sample)){
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

    static boolean enemyCanTakeFromCloud(Sample sample){
        if(sample.getCarriedBy() != -1){
            return false;
        }
        //如果敌人手上满了
        if(enemy.getSamples().size()==3 || enemy.getMoleculeCount() == 10){
            return false;
        }
        return enemy.envTypesReadyForSampleWithHelp(sample)
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
        private final State state;


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

        private int helpScore(Robot robot){
            int score = 0;
            for(Sample s: robot.samples.values()){
                if(s.sampleId == sampleId){
                    continue;
                }
                if(s.getCostForRobot(expertiseGain,robot)>0){
                    score ++;
                }
            }
            return score;
        }

        private boolean canBeHelped(Robot robot,Sample helpSample){
            if(getCostForRobot(helpSample.expertiseGain,robot)>0){
                return true;
            }
            return false;
        }

        private boolean canBeHelped(Robot robot){
            for(Sample s: robot.samples.values()){
                if(s.sampleId == sampleId){
                    continue;
                }
                //s已经ready而且可以帮助自己
                if(robot.getRequiredCount(s)==0 && getCostForRobot(s.expertiseGain,robot)>0){
                    return true;
                }
            }
            return false;
        }

        @Override
        public int compareTo(Sample o) {
//            System.err.println(carriedBy+"哎"+o.carriedBy);
            if(state == State.undiagnosed && o.state == State.diagnosed){
                return -9999;
            }
            if(getCarriedBy() == 1 && o.getCarriedBy() == 0){
                return -1;
            }

            /*
            敌人的默认按分数高低排序
            当敌人的project只差1个的时候,按project需求排序
            当分数相同的，差的多的排前面
             */
            if(getCarriedBy() == 1 && o.getCarriedBy() == 1){
                return enemySampleCompare(o);
            }
            if(getCarriedBy() == 0 && o.getCarriedBy() == 0){
                return selfSampleCompare(o);
            }

            //默认按分数排序
            return o.health - health;
        }

        //Project如果差1个，优先级最高
        //如果在MOLECULES,看helpScore
        //然后按project，分数排序
        //最后一轮，按需求多少排序
        private Integer selfSampleCompare(Sample o) {
            Project chosenProject = null;
            boolean canContributeToChosenProject = false;
            if(canFinishProject(robot,enemy,this) &&!canFinishProject(robot,enemy,o)){
                return -1;
            }
            if(canFinishProject(robot,enemy,o) && !canFinishProject(robot,enemy,this)){
                return 1;
            }

            //help score高的排前面
            if((robot.location.equals(MOLECULES)||robot.location.equals(LABORATORY)) && robot.samples.size()>1){
                int a =  o.helpScore(robot) - helpScore(robot);
//                System.err.println(sampleId+" vs " + o.sampleId+", result="+a);
                if(a!=0){
                    return a;
                }
                if(canBeHelped(robot) && !o.canBeHelped(robot)){
                    return -1;
                }
                else if(o.canBeHelped(robot) && !canBeHelped(robot)){
                    return 1;
                }
            }

            //没有ready sample时，优先考虑需求少的
            if(robot.samples.size()>0 && robot.getReadySampleNumber()==0){
                int iRequired = robot.getRequiredCount(this);
                int oRequired = robot.getRequiredCount(o);
                int r = Math.max(iRequired,oRequired);
//                System.err.println(iRequired+"##"+oRequired+"##"+robot.envTypesReadyForSample(this)+"##"+robot.envTypesReadyForSample(o));
                //最后一次而且够拿,搞分数大的
                if(round>THRESHOLD && 200-round>= r + 4 && robot.envTypesReadyForSample(this) && robot.envTypesReadyForSample(o)){
                    return o.health - health;
                }
                else{
                    if(!robot.envTypesReadyForSample(this) && robot.envTypesReadyForSample(o)){
                        return 1;
                    }
                    else if(robot.envTypesReadyForSample(this) && !robot.envTypesReadyForSample(o)){
                        return -1;
                    }
                    return iRequired - oRequired;
                }
            }

            List<Project> chosenProjects = getChosenProjects(robot,enemy);
            if(!chosenProjects.isEmpty()){
                chosenProject = chosenProjects.get(0);
            }
            //如果需求最少的project只需要2个以下
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

                    int result = getChosenProjectRequiredScore(chosenProject,o.expertiseGain) - getChosenProjectRequiredScore(chosenProject,expertiseGain);
//                        System.err.println("排序策略是project优先:"+sampleId+" vs "+o.sampleId+",result="+result);
                    if(result == 0 || (round>THRESHOLD && chosenProject.getTotalRequired(robot)>=2)){
                        return defaultSelfCompare(o);
                    }
                    return result;
                }
            }
            return defaultSelfCompare(o);
        }

        private int defaultSelfCompare(Sample o){
            if((robot.location.equals(MOLECULES)||robot.location.equals(LABORATORY)) && robot.samples.size()>1){
                int a =  o.helpScore(robot) - helpScore(robot);
//                System.err.println(sampleId+" vs " + o.sampleId+", result="+a);
                if(a!=0){
                    return a;
                }
                if(canBeHelped(robot) && !o.canBeHelped(robot)){
                    return -1;
                }
                else if(o.canBeHelped(robot) && !canBeHelped(robot)){
                    return 1;
                }
            }
            //技能等于大于4的优先级低
            if(o.health - health == 0 && robot.getExpertiseByType(expertiseGain)>4){
                return 1;
            }
            return o.health - health;
        }

        private int enemySampleCompare(Sample o) {
            Project chosenProject = null;
            List<Project> chosenProjects = getChosenProjects(enemy,robot);
            if(!chosenProjects.isEmpty()){
                chosenProject = chosenProjects.get(0);
            }
            if(chosenProject!=null && chosenProject.getTotalRequired(enemy)==1){
                Type requiredType = null;
                for(Type type: Type.types){
                    if(chosenProject.getRequiredNumber(enemy,type.value) == 1){
                        requiredType = type;
                        break;
                    }
                }
                if(requiredType!=null){
                    for(Sample sample:enemy.samples.values()){
                        if(sample.getExpertiseGain().equals(requiredType.value)){
                            return -999;
                        }
                    }
                }
            }
            int r = o.health - health;
            if(r==0){
                return enemy.getRequiredCount(o) - enemy.getRequiredCount(this);
            }
            return r;
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

        static final Map<String, Type> TYPE_MAP = new HashMap<>();
        static {
            for (Type type : Type.values()) {
                TYPE_MAP.put(type.value, type);
            }
        }

        public static Type fromValue(String value) {
            return TYPE_MAP.get(value);
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
        boolean hasSampleInState(State state){
            for(Sample sample:samples.values()){
                if(sample.state == state){
                    return true;
                }
            }
            return false;
        }
        boolean hasDiggnosicSample(){
            return hasSampleInState(State.diagnosed);
        }

        boolean hasUnDiggnosicSample(){
            return hasSampleInState(State.undiagnosed);
        }

        boolean hasUrgentRequest(Sample sample){
            for(Sample s:samples.values()){
                if(s.sampleId == sample.sampleId){
                    continue;
                }
                for(Type type:Type.types){
                    if(robot.getRequiredCountCorrelation(type,s)==1 && env.getAvailableCountByType(type)==1){
                        return true;
                    }
                }
            }
            return false;
        }
        List<Sample> getReadySamples(){
            List<Sample> readySamples = new ArrayList<>();
            for(Sample sample:samples.values()){
                if(getRequiredCount(sample)==0){
                    readySamples.add(sample);
                }
            }
            readySamples.sort(Sample::compareTo);
            return readySamples;
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
            setCommand("CONNECT " + sampleId);
        }

        void putToCloud(int sampleId){
            setCommand("CONNECT " + sampleId);
        }

        void takeFromCloud(Sample sample){
            setCommand("CONNECT " + sample.getSampleId());
        }

        void diagnosSample(Sample sample){
            setCommand("CONNECT " + sample.getSampleId());
        }

        void takeMolecule(Type type){
            if(robot.getMoleculeCount()>=10 || env.getAvailableCountByType(type)==0){
                return;
            }
            setCommand("CONNECT " + type.value());
        }

        int getMoleculeCount(){
            return countA+countB+countC+countD+countE;
        }
        boolean envTypesReadyForSampleWithHelp(Sample sample){
            for(Type type:Type.types){
                int required = getRequiredCountCorrelation(type,sample);
                if(env.getAvailableCountByType(type)<required){
                    return false;
                }
            }
            return true;
        }

        boolean envTypesReadyForSample(Sample ...  samples){
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

        List<Sample> getPotentialReadySamples(){
            List<Sample> readySamples = new ArrayList<>();
            for(Sample sample: getSamples().values()){
                if(sample.getState() == State.diagnosed){
                    if(getRequiredCount(sample)<=10-getMoleculeCount() && envTypesReadyForSample(sample)){
                        readySamples.add(sample);
                    }
                }
            }
            readySamples.sort(Sample::compareTo);
            return readySamples;
        }

        List<Sample> getPotentialReadySamplesWithHelp(){
            List<Sample> readySamples = new ArrayList<>();
            for(Sample sample: getSamples().values()){
                if(sample.getState() == State.diagnosed){
                    if(getRequiredCount(sample)<=10-getMoleculeCount() && envTypesReadyForSampleWithHelp(sample)){
                        readySamples.add(sample);
                    }
                }
            }
            readySamples.sort(Sample::compareTo);
            return readySamples;
        }

        public int getTotalHealth(){
            int score = 0;
            for(Sample sample:samples.values()){
                score+=sample.getHealth();
            }
            return score;
        }

        public int getRequiredCount(Type type, int sampleId){
            return getRequiredCount(type,samples.get(sampleId));
        }

        int getRequiredCountConsiderCombinition(Type type,Sample sample){
            List<Sample> sss = new ArrayList<>(samples.values());
            sss.sort(Sample::compareTo);
            for(int i=0;i<sss.size();i++){
                Sample s = sss.get(i);
                if(i==0 && sample.getSampleId() == s.getSampleId()){
                    return getRequiredCount(type,sample);
                }
                if(i==1 && sample.getSampleId() == s.getSampleId()){
                    Sample preSample = sss.get(0);
                    if(getRequiredCount(type,preSample)==0){
                        int left = getCountByType(type.value) - preSample.getCostForRobot(type.value,this);
                        if(preSample.expertiseGain.equals(type.value)){
                            left++;
                        }
                        return Math.max(sample.getCostForRobot(type.value,this) - left,0);
                    }
                }
                if(i==2 && sample.getSampleId() == s.getSampleId()){
                    Sample preSample1 = sss.get(0);
                    Sample preSample2 = sss.get(1);
                    //1,2都ready
                    if(getRequiredCount(preSample1)==0 &&getRequiredCount(preSample1,preSample2)==0  ){
                        int left = getCountByType(type.value) - preSample1.getCostForRobot(type.value,this) - preSample2.getCostForRobot(type.value,this);
                        if(preSample1.expertiseGain.equals(type.value)){
                            left++;
                        }
                        if(preSample2.expertiseGain.equals(type.value)){
                            left++;
                        }
                        return Math.max(sample.getCostForRobot(type.value,this) - left,0);
                    }
                    //1ready
                    else if(getRequiredCount(preSample1)==0){
                        int left = getCountByType(type.value) - preSample1.getCostForRobot(type.value,this);
                        if(preSample1.expertiseGain.equals(type.value)){
                            left++;
                        }
                        return Math.max(sample.getCostForRobot(type.value,this) - left,0);
                    }
                    //2 ready
                    else if(getRequiredCount(preSample2)==0){
                        int left = getCountByType(type.value) - preSample2.getCostForRobot(type.value,this);
                        if(preSample2.expertiseGain.equals(type.value)){
                            left++;
                        }
                        return Math.max(sample.getCostForRobot(type.value,this) - left,0);
                    }
                }
            }
            return getRequiredCount(type,sample);
        }

        public int getRequiredCount(int sampleId){
            return getRequiredCount(samples.get(sampleId));
        }

        public int getRequiredCount(Sample sample){
            int count = 0;
            for(Type type:Type.types){
                count += getRequiredCount(type,sample);
            }
            return count;
        }

        public int getRequiredCountWithHelp(Sample sample, Sample helpSample){
            int count = 0;
            for(Type type:Type.types){
                count += getRequiredCountWithHelp(type,sample,helpSample);
            }
            return count;
        }

        private int getRequiredCountWithHelp(Type type, Sample sample, Sample helpSample){
            if(sample.getState() == State.undiagnosed){
                return 999;
            }

            int count = getRequiredCount(type,sample);
            if(count>0 && helpSample!=null && type.value == helpSample.expertiseGain){
                count--;
            }

            return Math.max(0,count);
        }

        public int getRequiredCountCorrelation(Type type,Sample sample){
            int count = getRequiredCount(type,sample);
            if(count ==0){
                return count;
            }
            boolean hasReduced = false;
            List<Sample> sss = new ArrayList<>(samples.values());
            sss.sort(Sample::compareTo);
            int reduceCount = 0;
            Sample firstReduceSample = null;
            for(Sample s: sss){
                if(s.sampleId==sample.sampleId){
                    continue;
                }
                if(reduceCount == 0){
                    if(getRequiredCount(s)==0){
                        Type helpType = Type.fromValue(s.expertiseGain);
                        if(getRequiredCount(helpType,sample)>0){
                            hasReduced = true;
                        }
                        //如果命中,跳出
                        if(type == helpType){
                            count --;
                            break;
                        }
                        else {
                            reduceCount++;
                            firstReduceSample = s;
                        }
                    }
                }
                else{
                    if(getRequiredCountWithHelp(s,firstReduceSample)==0){
                        Type helpType = Type.fromValue(s.expertiseGain);
                        if(type == helpType){
                            if(!hasReduced){
                                count --;
                            }
                            reduceCount++;
                            break;
                        }
                    }
                }
            }
            return count;
        }

        public int getRequiredCountCorrelation(Sample sample){
            int count = getRequiredCount(sample);
            if(count ==0){
                return count;
            }
            List<Sample> sss = new ArrayList<>(samples.values());
            sss.sort(Sample::compareTo);
            int reduceCount = 0;
            for(Sample s:sss){
                if(s.sampleId==sample.sampleId){
                    continue;
                }
                if(getRequiredCount(s)==0){
                    Type type = Type.fromValue(s.expertiseGain);
                    //第一个ready的
                    if(reduceCount == 0){
                        if(robot.getRequiredCount(type,sample)>0){
                            count --;
                            reduceCount++;
                        }
                    }
                    //第二个可能ready的满减
                    else{
                        if(robot.getRequiredCountCorrelation(type,sample)>0){
                            count --;
                            reduceCount++;
                        }
                    }

                }
            }
            return Math.max(count,0);
        }

        public int getMaxRequiredTypeCount(Sample sample){
            int count = 0;
            for(Type type:Type.types){
                int c = getRequiredCount(type,sample);
                if(c>0 && c>count){count = c;}
            }
            return count;
        }

        public Type getMaxRequiredType(Sample sample){
            int count = 0;
            Type t = null;
            for(Type type:Type.types){
                int c = getRequiredCount(type,sample);
                if(c>0 && c>count){count = c;t = type;}
            }
            return t;
        }


        public Set<Type> getRequiredTypes(Set<Integer> readySamples){
            Set<Type> typeSet = new HashSet<>();
            for(int id: readySamples){
                if(getRequiredCount(Type.A,id)>0){typeSet.add(Type.A);}
                if(getRequiredCount(Type.B,id)>0){typeSet.add(Type.B);}
                if(getRequiredCount(Type.C,id)>0){typeSet.add(Type.C);}
                if(getRequiredCount(Type.D,id)>0){typeSet.add(Type.D);}
                if(getRequiredCount(Type.E,id)>0){typeSet.add(Type.E);}
            }
            return typeSet;
        }

        int getRequiredCountFromReadySamples(Type requiredType){
            List<Sample> readySamples = getReadySamples();
            int count = 0;

            if(readySamples.size() == 2){
                Sample s1 = readySamples.get(0);
                Sample s2 = readySamples.get(1);
                boolean bothReady = true;
                for(Type type:Type.types){
                    if(getRequiredCount(type,s1,s2) > 0){
                        bothReady = false;break;
                    }
                }
                int c1 = s1.getCostForRobot(requiredType.value,this);
                int c2 = s2.getCostForRobot(requiredType.value,this);
                if(bothReady){
                    count = c1 + c2;
                    if(s1.expertiseGain.equals(requiredType.value)||s2.expertiseGain.equals(requiredType.value)){count --;}
                }
            }
            else if(readySamples.size() == 3){
                Sample s1 = readySamples.get(0);
                Sample s2 = readySamples.get(1);
                Sample s3 = readySamples.get(2);
                boolean allReady = true;
                for(Type type:Type.types){
                    if(getRequiredCount(type,s1,s2,s3) > 0){
                        allReady = false;
                        break;
                    }
                }
                int c1 = s1.getCostForRobot(requiredType.value,this);
                int c2 = s2.getCostForRobot(requiredType.value,this);
                int c3 = s2.getCostForRobot(requiredType.value,this);
                boolean _1_2_Ready = true;
                for(Type type:Type.types){
                    if(getRequiredCount(type,s1,s2) > 0){
                        _1_2_Ready = false;
                        break;
                    }
                }

                boolean _1_3Ready = true;
                for(Type type:Type.types){
                    if(getRequiredCount(type,s1,s3) > 0){
                        _1_3Ready = false;
                        break;
                    }
                }
                boolean _2_3_Ready = true;
                for(Type type:Type.types){
                    if(getRequiredCount(type,s2,s3) > 0){
                        _2_3_Ready = false;break;
                    }
                }
                if(allReady){
                    count = c1+c2+c3;
                    int rr =0;
                    for(Sample s:readySamples){
                        if(s.expertiseGain.equals(requiredType.value)){
                            rr++;
                        }
                    }
                    if(rr>1){count = count - (rr-1);}
                }
                else{
                    if(_1_2_Ready){
                        count = c1 + c2;
                        if(s1.expertiseGain.equals(requiredType.value)||s2.expertiseGain.equals(requiredType.value)){
                            count --;
                        }
                    }
                    else if(_1_3Ready){
                        count = c1 + c3;
                        if(s1.expertiseGain.equals(requiredType.value)||s3.expertiseGain.equals(requiredType.value)){
                            count --;
                        }
                    }
                    else if(_2_3_Ready){
                        count = c2 +c3;
                        if(s2.expertiseGain.equals(requiredType.value)||s2.expertiseGain.equals(requiredType.value)){
                            count --;
                        }
                    }
                }
            }
            if(count ==0 && readySamples.size()>0){
                count = readySamples.get(0).getCostForRobot(requiredType.value,this);
            }
            return Math.max(count,0);
        }
        public int getRequiredCount(Set<Integer> readySamples) {
            int count = 0;
            for (Type type : Type.types) {
                count += getRequiredCount(type, readySamples);
            }
            return count;
        }
        private int getRequiredCount(Type type, Sample sample){
            if(sample.getState() == State.undiagnosed){
                return 999;
            }
            int count = sample.getCostForRobot(type.value,this);
            count -= getCountByType(type.value);
            return Math.max(0,count);
        }
        public int getRequiredCount(Type type, Sample ... samples){
            Set<Integer> sampleSet = new HashSet<>();
            for(Sample sample: samples){
                sampleSet.add(sample.sampleId);
            }
            return getRequiredCount(type,sampleSet);
        }
        public int getRequiredCount(Sample ... samples){
            Set<Integer> sampleSet = new HashSet<>();
            for(Sample sample: samples){
                sampleSet.add(sample.sampleId);
            }
            return getRequiredCount(sampleSet);
        }
        public int getRequiredCount(Type type, Set<Integer> readySamples) {
            List<Sample> sampleList = new ArrayList<>();
            for (int id : readySamples) {
                sampleList.add(samples.get(id));
            }
            sampleList.sort(Sample::compareTo);
            if (readySamples.size() == 1) {
                return getRequiredCount(type, sampleList.get(0));
            }
            if (readySamples.size() == 2) {
                Sample s1 = sampleList.get(0);
                Sample s2 = sampleList.get(1);
                int left = getCountByType(type.value) - s1.getCostForRobot(type.value, this);
                left = Math.max(0,left);
                int required = s2.getCostForRobot(type.value, this) - left;
                if (s1.expertiseGain.equals(type.value) && required > 0) {
                    required--;
                }
                System.err.println("readySamples:"+readySamples+" 对于type"+type.value+"需要"+Math.max(0, required)+"个");
                return Math.max(0, required);
            }
            if (readySamples.size() == 3) {
                Sample s1 = sampleList.get(0);
                Sample s2 = sampleList.get(1);
                Sample s3 = sampleList.get(2);
                boolean _1Ready = getRequiredCount(type, s1)==0;
                boolean _2Ready = false;
                boolean _1reduced = false;
                int left = getCountByType(type.value)- s1.getCostForRobot(type.value, this);
                int _2required = s2.getCostForRobot(type.value, this);
                left = Math.max(0,left);
                _2required -= left;
                left = left - s2.getCostForRobot(type.value, this);
                left = Math.max(0,left);
                if (_1Ready && s1.expertiseGain.equals(type.value) && _2required > 0) {
                    _2required--;
                    _1reduced = true;
                }
                _2required = Math.max(0, _2required);
                if(_2required==0){
                    _2Ready = true;
                }
                int _3required = s3.getCostForRobot(type.value, this) - left;
                if(_3required>0 ){
                    if((_2Ready &&s2.expertiseGain.equals(type.value))||(_1Ready && s1.expertiseGain.equals(type.value) && !_1reduced)) {
                        _3required--;
                    }
                }
                _3required = Math.max(0, _3required);
                System.err.println(getRequiredCount(type, s1)+"..."+_2required+"..."+_3required);
                return getRequiredCount(type, s1) + _2required + _3required;
            }
            return 0;
        }
    }


    /*
    TODO:调整参数
     */
    static int getLevel() {

        if(robot.getMoleculeCount() == 10 && robot.getReadySampleNumber() == 0){
            return 1;
        }
        if(round<=100){
            return 1;
        }

        if(round<110){
            if(robot.getExpertise()<=7){
                return 1;
            }

            if(robot.getExpertise() <=9){
                if(robot.countLevel(2)>0){
                    return 1;
                }
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
        if(round>175){
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

        Env(){}

        Env(int a, int b, int c, int d, int e){
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
        }

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

        int getMinAvailable(Robot robot){
            int min = 999;
            for(Type type:Type.types){
                int c = robot.getCountByType(type.value) + env.getAvailableCountByType(type);
                min = Math.min(min,c);
            }
            return c;
        }
    }

    /*
    下一步敌人可能要拿起或者放下
     */
    private static int enemyGoingToRelease(Type type, int step){
        int count = 0;
        if(enemy.location.equals(LABORATORY) && !enemyWaitAndDefend()){
            count = enemy.getRequiredCountFromReadySamples(type);
        }
        /*
        如果敌人在材料台，他随后可能会释放多少个type X.
         */
        else if(enemy.location.equals(MOLECULES)&&enemy.eta <= step){
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
                        if(canDo){
                            /*
                             TODO:只算了第一个sample的释放的值
                            */
                            count =sample.getCostForRobot(type.value,enemy);
                            break;
                        }
                    }
                }
            }
        }
        return count;
    }

    static Map<String,Set<Sample>> enemyLeadSuprPassSamples(){
        boolean enemyLocation =  enemy.getLocation().equals(DIAGNOSIS) || enemy.getLocation().equals(MOLECULES);
        List<Sample> blackList = new ArrayList<>();
        Map<String,Set<Sample>> sMap = new HashMap<>();
        int gap = robot.score - enemy.score;
        if(!enemyLocation || gap<0){return sMap;}
        List<Sample> readySamples = enemy.getPotentialReadySamplesWithHelp();
        System.err.println("敌人潜在的readySamples"+readySamples);

        for(Sample sample:readySamples){
            //已经ready的，防不住
            if(enemy.getRequiredCount(sample)==0){continue;}
            int gain = sample.health;
            if(canFinishProject(enemy, robot,sample)){gain +=50;}
            if(gap<gain){blackList.add(sample);}
        }
        //某一个就能导致敌人分数超越
        if(!blackList.isEmpty()){
            if(blackList.size() == 1){
                sMap.put("all",new HashSet<>(blackList));
            }
            return sMap;
        }

        if(readySamples.size() == 2){
            Sample s1 = readySamples.get(0);
            Sample s2 = readySamples.get(1);
            tryToFillBlackList(gap, blackList, s1,s2);
            if(!blackList.isEmpty()){
                blackList.sort(Sample::compareTo);
                sMap.put("either",new HashSet<>(blackList));
                return sMap;
            }
        }
        else if(readySamples.size() == 3){
            Sample s1 = readySamples.get(0);
            Sample s2 = readySamples.get(1);
            Sample s3 = readySamples.get(2);
            tryToFillBlackList(gap, blackList, s1,s2);
            tryToFillBlackList(gap, blackList, s1,s3);
            tryToFillBlackList(gap, blackList, s2,s3);
            if(blackList.size()>2){return sMap;}
            if(blackList.isEmpty()){
                tryToFillBlackList(gap, blackList,s1,s2,s3);
            }
        }
        blackList.sort(Sample::compareTo);
        sMap.put("either",new HashSet<>(blackList));
        return sMap;
    }

    private static void tryToFillBlackList(int gap, List<Sample> blackList, Sample ... samples) {
        if(enemy.getRequiredCount(samples)<=0){return;}
        System.err.println("env里的对于"+samples.length+"个sample够不够?"+enemy.envTypesReadyForSample(samples));
        if(!enemy.envTypesReadyForSample(samples)){return;}
        int gain = 0;
        for(Sample sample:samples){
            gain += sample.health;
        }
        if(canFinishProject(enemy,robot,samples)){gain +=50;}
        if(gap<gain){
            blackList.addAll(new ArrayList<Sample>(Arrays.asList(samples)));return;}
    }

    static boolean canFinishProject(Robot robot, Robot enemy, Sample ... samples){
        Project.enemyUse=true;
        List<Project> projects = getChosenProjects(robot, enemy);
        if(projects.isEmpty()){
            return false;
        }
        for(Project project:projects){
            List<Type> required = new ArrayList<>();
            if(project.getTotalRequired(robot)>0 && project.getTotalRequired(robot)<=samples.length){
                for(Type type:Type.types){
                    if(project.getRequiredNumber(robot,type.value)==1){
                        required.add(type);
                    }
                    else if(project.getRequiredNumber(robot,type.value)==2){
                        required.add(type);
                        required.add(type);
                    }
                }
                if(required.isEmpty()){
                    return false;
                }
                System.err.println("完成project需要的types为:"+required);
                int len = required.size();
                for(Sample s:samples){
                    Type t = Type.fromValue(s.getExpertiseGain());
                    if(t!=null &&required.contains(t)){
                        len --;
                    }
                }
                if(len ==0){return true;}
            }
        }
        return false;
    }

    static boolean hasDefendCondition(String myLocation, Map<String,Set<Sample>> sMap){
        if(sMap == null || sMap.isEmpty()){
            return false;
        }
        String key = "";
        Set<Sample> samples = null;
        if(sMap.get("all")!=null){
            key = "all";
            samples = sMap.get("all");
            if(samples.size()>1){return false;}
        }
        else if(sMap.get("either")!=null){
            key = "either";
            samples = sMap.get("either");
        }
        if(samples == null || samples.isEmpty()){return false;}

        boolean stepValid = false;
        int validCount = 0;
        Type fType = null;
        if(key.equals("all")){
            for(Sample sample:samples){
                int fGap = 999, fRequired = -1;
                for(Type type:Type.types){
                    int required = enemy.getRequiredCount(type,sample);
                    if(required>0){
                        int helpCount = 0;
                        helpCount +=  helpTypeNumber(enemy,sample,type);
                        required -=helpCount;
                        System.err.println("对于sample"+sample.sampleId+",type"+type.value+",敌人还需要"+required+"个");
                        int gap = env.getAvailableCountByType(type) - required;
                        //先防守敌人需求最多的
                        if(gap>=0 && gap<=fGap && required>fRequired){
                            fGap = gap;
                            fRequired = required;
                            fType = type;
                        }
                    }
                }

                if(fGap<900){
                    int enemyStep,iStep;
                    if(enemy.location.equals(MOLECULES)){enemyStep = fRequired + enemy.eta;}
                    else if(enemy.location.equals(DIAGNOSIS) || enemy.location.equals(SAMPLES)){
                        enemyStep = fRequired + 3 + enemy.eta;
                        long count =  enemy.samples.values().stream().filter(item->item.state == State.undiagnosed).count();
                        enemyStep += count;
                    }
                    else{
                        enemyStep = fRequired + 3 + enemy.eta;
                        if(enemy.getReadySampleNumber()>0){enemyStep++;}
                    }
                    if(robot.location.equals(MOLECULES)){iStep = fGap + 1 + robot.eta;}
                    else{iStep = fGap + 1 + robot.eta + 3;}
                    System.err.println("为了完成sample"+sample.sampleId+",我需要"+iStep+"步，敌人需要"+enemyStep+"步拿"+fType.value);
                    if(iStep < enemyStep && robot.getMoleculeCount()+fGap < 10){
                        stepValid = true;
                        validCount ++;
                    }
                }
            }
        }
        else if(key.equals("either")){
            int ffRequired = 0, ffGap =9999;
            for(Sample sample:samples) {
                int fGap = 999, fRequired = -1;
                for (Type type : Type.types) {
                    int required = enemy.getRequiredCount(type, sample);
                    if (required > 0) {
                        int helpCount = 0;
                        helpCount += helpTypeNumber(enemy, sample, type);
                        required -= helpCount;
                        int gap = env.getAvailableCountByType(type) - required;
                        //先防守敌人需求最多的
                        if (gap >= 0 && gap <= fGap && required > fRequired) {
                            fGap = gap;
                            fRequired = required;
                        }
                    }
                }
                if(fRequired>0){ffRequired +=fRequired;}
                if(fGap<900){ffGap = Math.min(ffGap,fGap);}
            }
            if(ffGap<900){
                int enemyStep,iStep;
                if(enemy.location.equals(MOLECULES)){enemyStep = ffRequired + enemy.eta;}
                else{enemyStep = ffRequired + 3;}
                if(robot.location.equals(MOLECULES)){iStep = ffGap + 1 + robot.eta;}
                else{iStep = ffGap + 1 + robot.eta + 3;}
                System.err.println("为了完成project,需要完成的Samples:长度="+samples.size()+",我最少需要"+iStep+"步来破坏，敌人总共需要"+enemyStep+"步");
                if(iStep < enemyStep && robot.getMoleculeCount()+ffGap < 10){stepValid = true;validCount ++;}
            }
        }
        if(key.equals("all")){stepValid =  stepValid && validCount ==1;}
        return stepValid && hasDefendCondition(myLocation);
    }

    static boolean hasProjectDefendCondition(String myLocation,Map<String,Set<Sample>> sMap){
        return hasDefendCondition(myLocation,sMap) && round < THRESHOLD;
    }

    static boolean hasFinalDefendCondition(String myLocation,Map<String,Set<Sample>> sMap){
        return round>=THRESHOLD && hasDefendCondition(myLocation,sMap) && (robot.score > enemy.score);
    }

    private static boolean hasDefendCondition(String myLocation){
        boolean enemyLocation;
        switch (myLocation){
            case MOLECULES:
                enemyLocation = enemy.getLocation().equals(SAMPLES) ||enemy.getLocation().equals(DIAGNOSIS) || (enemy.getLocation().equals(LABORATORY) && enemy.getReadySampleNumber() ==0) || (enemy.getLocation().equals(MOLECULES) && enemy.eta>=0);
                break;
            default:enemyLocation = enemy.getLocation().equals(SAMPLES) ||enemy.getLocation().equals(DIAGNOSIS) || (enemy.getLocation().equals(LABORATORY) && enemy.getReadySampleNumber() ==0) || (enemy.getLocation().equals(MOLECULES) && enemy.eta>=2);
        }
        return enemyLocation;
    }

    static int previousEnemyMoleculeCount = -1; static String previousRoundEnemyLocation = ""; static int previousRoundEnemyEta = -1;

    /**
     * 敌人是不是在防守等待
     * @return
     */
    static boolean enemyWaitAndDefend(){
        boolean stop = enemy.getLocation().equals(LABORATORY) && LABORATORY.equals(previousRoundEnemyLocation) && enemy.eta ==0 && 0 == previousRoundEnemyEta && enemy.getMoleculeCount() == previousEnemyMoleculeCount;
        return stop;
    }

    /**
     * 敌人是不是在防守等待
     * @return
     */
    static boolean enemyWaitForRelease(){
        boolean stop = enemy.getLocation().equals(MOLECULES) && MOLECULES.equals(previousRoundEnemyLocation) && enemy.eta ==0 && 0 == previousRoundEnemyEta && enemy.getMoleculeCount() == previousEnemyMoleculeCount;
        return stop;
    }

    //需要破坏其中一个,或者破坏全部
    static Map<String,Set<Sample>> sampleLeadEnemyFinishProject(){
        Map<String,Set<Sample>> rMap = new HashMap<>();
        List<Sample> sampleList = enemy.getPotentialReadySamplesWithHelp();
        System.err.println("potential ready list:" + sampleList);
        List<Sample> result = new ArrayList<>();
        for(Sample sample : sampleList){
            if(canFinishProject(enemy,robot,sample)){
                System.err.println("sample"+sample.sampleId+" will lead project finish,type"+enemy.getMaxRequiredType(sample)+",还需要"+enemy.getMaxRequiredTypeCount(sample)+"个");
                if(enemy.getRequiredCount(sample)>0){
                    if(result.size()==1){
                        Sample s = result.get(0);
                        System.err.println("sample"+s.sampleId+",type"+enemy.getMaxRequiredType(s)+",还需要"+enemy.getMaxRequiredTypeCount(s)+"个");
                        if(enemy.getMaxRequiredType(sample) == enemy.getMaxRequiredType(s)){
                            if(enemy.getMaxRequiredTypeCount(sample)<enemy.getMaxRequiredTypeCount(s)){result.clear();result.add(sample);}
                        }
                        else{result.add(sample);}
                    }
                    result.add(sample);
                }
                else{
                    System.err.println("悲剧,sample"+sample.sampleId+" lead project finish,已经满足了！！！");return new HashMap<>();
                }
            }
        }
        if(!result.isEmpty()){rMap.put("all",new HashSet<>(result));}
        else{
            Sample[] samples = sampleList.toArray(new Sample[sampleList.size()]);
            if(samples.length == 2){
                if(canFinishProject(enemy,robot,samples)){
                    System.err.println("samples[:"+samples[0].sampleId+","+samples[1].sampleId+"] lead project finish");
                    result.addAll(sampleList);
                    rMap.put("either",new HashSet<>(result));
                }
            }
            if(samples.length == 3){
                if(canFinishProject(enemy,robot, new Sample[]{samples[0],samples[1]})){
                    System.err.println("samples[:"+samples[0].sampleId+","+samples[1].sampleId+"] lead project finish");
                    result.add(sampleList.get(0));
                    result.add(sampleList.get(1));
                    rMap.put("either",new HashSet<>(result));
                }
                if(canFinishProject(enemy,robot,new Sample[]{samples[0],samples[2]})){
                    System.err.println("samples[:"+samples[0].sampleId+","+samples[2].sampleId+"] lead project finish");
                    result.add(sampleList.get(0));
                    result.add(sampleList.get(2));
                    if(rMap.get("either") == null){
                        rMap.put("either",new HashSet<>(result));
                    }
                    else{return new HashMap<>();}
                }
                if(canFinishProject(enemy,robot,new Sample[]{samples[1],samples[2]})){
                    System.err.println("samples[:"+samples[1].sampleId+","+samples[2].sampleId+"] lead project finish");
                    result.add(sampleList.get(1));
                    result.add(sampleList.get(2));
                    if(rMap.get("either") == null){
                        rMap.put("either",new HashSet<>(result));
                    }
                    else{return new HashMap<>();}
                }
                if((rMap.get("either") == null && canFinishProject(enemy,robot,samples))){
                    System.err.println("samples[:"+samples[0].sampleId+","+samples[1].sampleId+","+samples[2].sampleId+"] lead project finish");
                    result.addAll(sampleList);
                    rMap.put("either",new HashSet<>(result));
                }
            }
        }
        return rMap;
    }
    static boolean canWait(){
        int max =0;
        for(Sample sample:enemy.getReadySamples()){
            int score = sample.health;
            if(canFinishProject(enemy,robot,sample)){
                score +=50;
            }
            max = Math.max(max,score);
        }
        return !enemyWaitAndDefend() || robot.score>enemy.score+max;
    }
    static int helpTypeNumber(Robot robot, Sample sample, Type helpType){
        List<Sample> pReadySampleList = robot.getPotentialReadySamples();
        List<Type> types = new ArrayList<>();
        for(Sample s : pReadySampleList){
            if(s.sampleId == sample.sampleId){
                continue;
            }
            Type type = Type.fromValue(s.expertiseGain);
            if(type!=null && robot.getRequiredCount(type,sample)>0){
                types.add(type);
            }
        }
        int c = 0;
        if(!types.isEmpty()){
            for(Type t:types){
                if(t == helpType){
                    c++;
                }
            }
        }
        return c;
    }
}