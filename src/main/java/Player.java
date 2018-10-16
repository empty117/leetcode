import java.util.*;

/**
 * Bring data on patient samples from the diagnosis machine to the laboratory with enough molecules to produce medicine!
 * TODO:
 * - sample排序，按分数还是按project需求?
 * - 取材料的策略，如果有多个sample需求，则优先取？
 **/
class Player {

    private static final String SAMPLES = "SAMPLES", DIAGNOSIS = "DIAGNOSIS", MOLECULES = "MOLECULES", LABORATORY = "LABORATORY";

    private static Robot robot = new Robot();
    private static Robot enemy = new Robot();
    private static String command = "";
    private static int sCount = 0;
    private static Env env = new Env();
    static Project[] projects = new Project[3];
    //从材料台取不够材料
    static boolean giveUp = false;
    //最少从分析台拿够2个才走
    static int minCarryNumber =2;

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
    private static int go = -1;
    private static class Project implements Comparable<Project>{
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

        boolean chose(String type){
            switch (type){
                case "A":return a>0;
                case "B":return b>0;
                case "C":return c>0;
                case "D":return d>0;
                case "E":return e>0;
            }
            return false;
        }

        @Override
        public int compareTo(Project o) {
            return getTotalRequired(robot) - o.getTotalRequired(robot);
        }
    }

    private static int leftStep(){
//        if(round<=150){
//            return 2;
//        }
//        else if(round<=170){
//            return 1;
//        }
        return -999;
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
            int required = projects[i].getRequiredNumber(robot,type);
            System.err.println("project " + i + "一共需要"+projects[i].getTotalRequired(robot)+"个type，需要"+required+"个type"+type);
            if(required>0){
                score+=(3-i);
            }
        }
        System.err.println("project score for type"+type+"="+score );
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
                if (i == 0) {
                    robot.expertiseA = expertiseA;
                    robot.expertiseB = expertiseB;
                    robot.expertiseC = expertiseC;
                    robot.expertiseD = expertiseD;
                    robot.expertiseE = expertiseE;
                    robot.eta = eta;
                    robot.score = score;
                    robot.location=target;
                }
                if (i == 1) {
                    enemy.expertiseA = expertiseA;
                    enemy.expertiseB = expertiseB;
                    enemy.expertiseC = expertiseC;
                    enemy.expertiseD = expertiseD;
                    enemy.expertiseE = expertiseE;
                    enemy.eta = eta;
                    enemy.score = score;
                    enemy.location=target;
                }

            }
//            for(int i=0;i<projectCount;i++){
//
//                int total = projects[i].getTotalRequired(robot);
//                int enemyTotal = projects[i].getTotalRequired(enemy);
//                System.err.println("第"+i+"个project,还差"+total+"个,敌人还差"+enemyTotal+"个");
//                if(go == -1){
//                    if(total<=leftStep() && total<enemyTotal){
//                        System.err.println("我决定冲刺第"+i+"个project,还差"+total+"个,敌人还差"+enemyTotal+"个");
//                        go = i;
//                    }
//                }
//                /*
//                冲刺的project已完成
//                 */
//                else if(total<=0){
//                    go=-1;
//                }
//            }
//            if(go>=0){
//                System.err.println("我正在冲刺第"+go+"个project");
//            }
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
            boolean flag = false;
            String location = robot.location;
            int eta = robot.eta;
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
            System.err.println("Before的命令是:" + command);
            machine.before();
            sampleList.sort(Sample::compareTo);
            for(Sample sample: sampleList){
                System.err.println(sample);
                machine.execute(sample);
            }
            System.err.println("Execute的命令是:" + command);
            machine.executeFinal();
            System.err.println("Final的命令是:" + command);
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
     * 如果取了2个，敌人还在取,也走,抢先机
     */
    private static class SampleMachine implements Machine{
        @Override
        public void before() {
            if(!robot.location.equals(SAMPLES) || robot.eta>0){
                setCommand("GOTO " + SAMPLES);
            }
            if(sCount == 0){
                setCommand("CONNECT " + getLevel());
            }
        }

        @Override
        public void execute(Sample sample) {
        }

        @Override
        public void executeFinal() {
            int level = getLevel();
            if(round >=180 && level>1){
                level --;
                minCarryNumber = 1;
            }
            //如果没拿满
            if(robot.getSamples().size()<3){
                //最后几步了，拿起就走
                if(round>=180 && robot.getSamples().size()>0){
                    boolean hasUndiagnosed = false;
                    for(Sample sample:robot.getSamples().values()){
                        if(sample.getState() == State.undiagnosed){
                            hasUndiagnosed = true;
                            break;
                        }
                    }
                    //有至少一个未分析的
                    if(hasUndiagnosed){
                        setCommand("GOTO " + DIAGNOSIS);
                    }
                    else{
                        setCommand("CONNECT " + level);
                    }
                }
                else if(robot.getSamples().size() == 2){
                    //敌人还在拿就先走,抢先一步
                    if(round<150 && enemy.getLocation().equals(SAMPLES) && enemy.getEta() == 0 && enemy.getSamples().size()>=2){
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
        }

        @Override
        public void execute(Sample sample) {
            /*
            回来放弃的一定是没有ready的
            可能是拿满了
            可能是资源不够了
             */
            if(giveUp&&command.isEmpty()){
                System.err.println("尝试放弃一个");
                int chosenId=-1, score=0;
                //优先尝试丢差的最多的
                for(Sample s:robot.getSamples().values()){
                    //
                    if(s.getState() == State.diagnosed && (!s.isValid() || robot.getMoleculeCount() == 10)){
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
                    if(!sample.isValid() || (go>=0 && !projects[go].chose(sample.getExpertiseGain()))){
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
                else if(sample.isValid() && robot.getSamples().size()<3){
                    //没有冲刺,或者符合冲刺目标
                    if((go<0 || projects[go].chose(sample.getExpertiseGain()))){
                        robot.takeFromCloud(sample);
                    }
                }
            }
        }

        @Override
        public void executeFinal() {
            if(robot.getMoleculeCount()==10 && robot.hasReadySample()){
                setCommand("GOTO " + LABORATORY);
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
                //自己是ready了
                for(Type type: Type.types){
                    ////需要拿而且有
//                    System.err.println("还需要"+robot.getRequiredCount(type,sample.getSampleId())+"个"+type+",还有"+env.getAvailableCountByType(type)+"个");
                    if(robot.getRequiredCount(type,sample)>0 && env.getAvailableCountByType(type)>0){
//                        System.err.println("需要拿"+type);
                        robot.takeMolecule(type);
                        break;
                    }
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

            Set<Type> typeSet = robot.getRequiredTypes(readySamples);

            boolean readySamplesDone = typeSet.size() == 0;
            System.err.println("readySamples" + readySamples);


            /*
            如果ready sample拿不满了
            要么满10个了
            要么资源不够了
             */
            if(!readySamplesDone){
                setCommand("GOTO " + LABORATORY);
            }
            //readySample拿满了，尝试拿没ready的
            else{
                for(int id:noReadySamples){
                    for(Type type:typeSet){
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
                    //看能不能等
                    else{
                        //如果敌人在放,就等等
                        if(enemy.getLocation().equals(LABORATORY)){
                            setCommand("WAIT");
                        }
                        //敌人手上有至少5个，可能要去释放了,再等等
                        else if(enemy.getLocation().equals(MOLECULES) && enemy.getEta()==0 && env.getAll()+ robot.getMoleculeCount()<=20){
                            setCommand("WAIT");
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
//
//                else{
//                    if(command.isEmpty()){
//                        limitResource = 1;
//                        System.err.println("Set limitResource flag to 1");
//                    }
//                    setCommand("GOTO " + DIAGNOSIS);
//                }
            }
        }
    }

    private static class LabMachine implements Machine{
        @Override
        public void before() {
            if(robot.eta>0){
                setCommand("GOTO " + LABORATORY);
            }
        }

        @Override
        public void execute(Sample sample) {
            if(sample.getCarriedBy()==0){
                if(robot.getRequiredCount(sample.getSampleId()) ==0){
                    robot.produceOnLab(sample.getSampleId());
                }
            }
        }

        @Override
        public void executeFinal() {
            if(!command.isEmpty()){
                return;
            }
            /*
            如果手头少于一个sample而且分数小于30
            或者最后几轮手里没sample了
             */
            if((robot.getSamples().size()<=1 &&robot.getTotalHealth()<30) || (robot.getSamples().size()==0 && round>=180)){
                if((enemy.getLocation().equals(MOLECULES)
                        || enemy.getLocation().equals(LABORATORY)
                        || (enemy.getLocation().equals(SAMPLES) && enemy.getEta()>1))
                        &&validSampleInCloud()>= 3 - robot.getSamples().size()-1){
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
                        if(env.getAvailableCountByType(type)+enemyGoingToRelease(type)<requiredNumber || requiredNumber+robot.getMoleculeCount()>10){
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
                else if((enemy.getLocation().equals(MOLECULES)
                        || enemy.getLocation().equals(LABORATORY)
                        || (enemy.getLocation().equals(SAMPLES) && enemy.getEta()>0))
                        && validSampleInCloud()>=1){
                    setCommand("GOTO " + DIAGNOSIS);
                }
                else{
                    setCommand("GOTO " + SAMPLES);
                }
            }
        }
    }

    static int validSampleInCloud(){
        int count =0;
        for(Sample sample:sampleInCloud.values()){
            if(sample.isValid()){
                count++;
            }
        }
        return count;
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

        public int getCostAForRobot() {
            return Math.max(costA - robot.expertiseA,0);
        }

        public int getCostBForRobot() {
            return Math.max(costB - robot.expertiseB,0);
        }

        public int getCostCForRobot() {
            return Math.max(costC - robot.expertiseC,0);
        }

        public int getCostDForRobot() {
            return Math.max(costD - robot.expertiseD,0);
        }

        public int getCostEForRobot() {
            return Math.max(costE - robot.expertiseE,0);
        }

        public int getTotolCostForRobot() {
            return getCostAForRobot() +
                    getCostBForRobot() +
                    getCostCForRobot() +
                    getCostDForRobot() +
                    getCostEForRobot();
        }

        /**
         * 静态合法
         * 需要的type至少有一个可以取
         * @return
         */
        boolean isValid(){
            boolean staticValid = getCostAForRobot()<=5 && getCostBForRobot()<=5
                    && getCostCForRobot()<=5 && getCostDForRobot()<=5
                    && getCostEForRobot()<=5 && getTotolCostForRobot()<=10;
            return staticValid &&isEnvValid();
        }

        //动态评估是否可以
        boolean isEnvValid(){
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
                result = result && env.a+ enemyGoingToRelease(Type.A)>=a;
            }
            if(b>0){
                result = result && env.b+ enemyGoingToRelease(Type.B)>=b;
            }
            if(c>0){
                result = result && env.c+ enemyGoingToRelease(Type.C)>=c;
            }
            if(d>0){
                result = result && env.d+ enemyGoingToRelease(Type.D)>=d;
            }
            if(e>0){
                result = result && env.e+ enemyGoingToRelease(Type.E)>=e;
            }
            return result;
        }

        @Override
        public int compareTo(Sample o) {
//            return getProjectRequiredScore(o.expertiseGain) - getProjectRequiredScore(expertiseGain);

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

        boolean hasReadySample(){
            for(Sample sample:samples.values()){
                if(getRequiredCount(sample)==0){
                    return true;
                }
            }
            return false;
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
            if(command.isEmpty()){
                Sample sample =  samples.get(sampleId);
                int a = sample.getCostAForRobot();
                int b = sample.getCostBForRobot();
                int c = sample.getCostCForRobot();
                int d = sample.getCostDForRobot();
                int e = sample.getCostEForRobot();
                if(a>0 && countA >=a){
                    countA -=a;
                }
                if(b>0 && countB >=b){
                    countB -=b;
                }
                if(c>0 && countC >=c){
                    countC -=c;
                }
                if(d>0 && countD >=d){
                    countD -=d;
                }
                if(e>0 && countE >=e){
                    countE -=e;
                }
            }
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
            if(command.isEmpty()){
                switch (type){
                    case A: countA++;break;
                    case B: countB++;break;
                    case C: countC++;break;
                    case D: countD++;break;
                    case E: countE++;break;
                }
            }
            setCommand("CONNECT " + type.value());
        }

        int getMoleculeCount(){
            return countA+countB+countC+countD+countE;
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
                    count += sample.getCostAForRobot();
                    count -= countA;
                    break;
                case B:
                    count += sample.getCostBForRobot();
                    count -= countB;
                    break;
                case C:
                    count += sample.getCostCForRobot();
                    count -= countC;
                    break;
                case D:
                    count += sample.getCostDForRobot();
                    count -= countD;
                    break;
                case E:
                    count += sample.getCostEForRobot();
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

        public int getRequiredCount(Set<Integer> readySamples){
            int count =0;
            for(int id: readySamples){
                count = count
                        + samples.get(id).getCostAForRobot()
                        + samples.get(id).getCostBForRobot()
                        + samples.get(id).getCostCForRobot()
                        + samples.get(id).getCostDForRobot()
                        + samples.get(id).getCostEForRobot();
                count = count - countA - countB - countC - countD - countE;
            }
            return Math.max(count,0);
        }


        public Set<Type> getRequiredTypes(Set<Integer> readySamples){
            Set<Type> typeSet = new HashSet<>();
            int count =0;
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

        /*
        找出readySamples需要的type个数
         */
        public int getRequiredCount(Type type, Set<Integer> readySamples){
            int count = 0;
            switch (type){
                case A:
                    for(int id: readySamples){
                        count +=samples.get(id).getCostAForRobot();
                    }
                    count -= countA;
                    break;
                case B:
                    for(int id: readySamples){
                        count +=samples.get(id).getCostBForRobot();
                    }
                    count -= countB;
                    break;
                case C:
                    for(int id: readySamples){
                        count +=samples.get(id).getCostCForRobot();
                    }
                    count -= countC;
                    break;
                case D:
                    for(int id: readySamples){
                        count +=samples.get(id).getCostDForRobot();
                    }
                    count -= countD;
                    break;
                case E:
                    for(int id: readySamples){
                        count +=samples.get(id).getCostEForRobot();
                    }
                    count -= countE;
                    break;
            }
            return Math.max(count,0);
        }

    }

    public static int getLevel() {
        //小于4
        if(robot.getExpertise()<4){
            return 1;
        }
        if(robot.getMoleculeCount() == 10 && !robot.hasReadySample()){
            return 1;
        }
        //4-6
        else if(robot.getExpertise()<=6){
            if(robot.countLevel(2)>1){
                return 1;
            }
            return 2;
        }
        //如果可用的资源有限且敌人没意图释放
//        if(env.getMinAvailable()<=1){
//            //敌人准备释放
//            if(enemy.getLocation().equals(LABORATORY)||(enemy.getLocation().equals(MOLECULES) && enemy.getEta() ==0)){
//
//            }
//            else{
//                return 1;
//            }
//        }
        else if(go>=0){
            return 2;
        }
        //7-9
        else if(robot.getExpertise()<=9){
            if(robot.countLevel(3)>=1) {
                return 2;
            }
            else {
                return 3;
            }
        }
        //9-11
        else{
            if(robot.countLevel(3)>=2){
                return 2;
            }
            else {
                return 3;
            }
        }
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

    private static int getEnemyHoldNumber(Type type){
        int result =0;
        switch (type){
            case A: result = 5-env.a-robot.countA;break;
            case B: result = 5-env.b-robot.countB;break;
            case C: result = 5-env.c-robot.countC;break;
            case D: result = 5-env.d-robot.countD;break;
            case E: result = 5-env.e-robot.countE;break;
        }
        return result>0?result:0;
    }

    private static int enemyGoingToRelease(Type type){
        int count = 0;
        if(enemy.location.equals(LABORATORY)){
            for(Sample sample:enemy.getSamples().values()){
                //ready one
                if(robot.getRequiredCount(sample)<=0){
                    count+=robot.getRequiredCount(type,sample);
                }
            }
        }
        else if(enemy.location.equals(MOLECULES)&&enemy.eta==0){
            count +=getEnemyHoldNumber(type);
        }
        return count;
    }
}