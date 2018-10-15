import java.util.*;

/**
 * Bring data on patient samples from the diagnosis machine to the laboratory with enough molecules to produce medicine!
 * TODO:
 * - level的判定
 * - 从lab返回sample或者DIAGNOSIS的判定
 * - 冲刺project的判定
 * - 拿rank3的临界点
 * - 资源紧张时的策略
 **/
class Player {

    private static final String SAMPLES = "SAMPLES", DIAGNOSIS = "DIAGNOSIS", MOLECULES = "MOLECULES", LABORATORY = "LABORATORY";

    private static Robot robot = new Robot();
    private static Player enemy = new Player();
    private static String command = "";
    private static int sCount = 0;
    private static Env env = new Env();
    static Project[] projects = new Project[3];
    static int limitResource = -1;

    static Map<Integer,Sample> sampleInCloud;
    static Map<Integer,Sample> sampleInEnemyhand;

    private static void setCommand(String cmd){
        if(command.isEmpty()){
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
    private static int go = -1;
    private static class Project{
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

        void reduce(String type){
            switch (type){
                case "A":if(a>0){a--;} return;
                case "B":if(b>0){b--;}return;
                case "C":if(c>0){c--;}return;
                case "D":if(d>0){d--;}return;
                case "E":if(e>0){e--;}return;
            }
        }
    }

    private static int leftStep(){
        if(round<=150){
            return 2;
        }
        else if(round<=170){
            return 1;
        }
        return -999;
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
        }
        // game loop
        while (true) {
            command = "";
            round++;
            for(int i=0;i<projectCount;i++){
                System.err.println("第"+i+"个project,还差"+projects[i].getTotal()+"个");
                if(go == -1){
                    if(projects[i].getTotal()<=leftStep()){
                        //落后20分了，拼了
                        if(enemy.score-robot.score>=20){
                            System.err.println("我决定冲刺第"+i+"个project,还差"+projects[i].getTotal()+"个");
                            go = i;
                        }
                    }
                }
                /*
                冲刺的project已完成
                 */
                else if(projects[go].getTotal()<=0){
                    go=-1;
                }
            }
            if(go>=0){
                System.err.println("我正在冲刺第"+go+"个project");
            }
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
                    enemy.setExpertiseA(expertiseA);
                    enemy.setExpertiseB(expertiseB);
                    enemy.setExpertiseC(expertiseC);
                    enemy.setExpertiseD(expertiseD);
                    enemy.setExpertiseE(expertiseE);
                    enemy.setLocation(target);
                    enemy.setEta(eta);
                    enemy.score = score;
                }

            }
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
            System.err.println("location="+location+",sampleCount="+sampleCount + ",player.getEta="+eta);
            Machine machine = getMachine(location);
            machine.before();
            System.err.println("Before的命令是:" + command);
            sampleInCloud = new HashMap<>();
            sampleInEnemyhand = new HashMap<>();
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
                if(sample.getCarriedBy() == 1){
                    sampleInEnemyhand.put(sampleId,sample);
                }
                machine.execute(sample);
            }
            System.err.println("robot持有"+robot.getMoleculeCount()+"个molecule,"+robot.getSamples().size()+"个sample");
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
            System.err.println(sample);
            //刷新库存
            if(sample.getCarriedBy()==0){
                robot.getSamples().putIfAbsent(sample.getSampleId(),sample);
            }
        }

        @Override
        public void executeFinal() {
            if(robot.getSamples().size()<3){
                if(robot.getSamples().size() == 2){
                    //敌人还在拿就先走,抢先一步
                    if(round<150 && enemy.getLocation().equals(SAMPLES) && enemy.getEta() == 0 && sampleInEnemyhand.size()>=2){
                        setCommand("GOTO " + DIAGNOSIS);
                    }
                    else{
                        setCommand("CONNECT " + getLevel());
                    }
                }
                else{
                    setCommand("CONNECT " + getLevel());
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
            }
        }

        @Override
        public void execute(Sample sample) {
            System.err.println(sample);
            if(sample.carriedBy==0){
                robot.getSamples().put(sample.getSampleId(),sample);
                //未分析的sample
                if(sample.getState() == State.undiagnosed){
                    robot.diagnosSample(sample);
                }
                else if(limitResource==1){
                    if(command.isEmpty()){
                        System.err.println("Set limitResource flag to 0");
                        limitResource = 0;
                    }
                    robot.putToCloud(sample.getSampleId());

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
                if(sample.isValid() && robot.getSamples().size()<3){
                    if(limitResource==0 && sample.getRank()<3){
                        if(command.isEmpty()){
                            System.err.println("Set limitResource flag to -1");
                            limitResource=-1;
                        }
                        robot.takeFromCloud(sample);

                    }
                    //没有冲刺,或者符合冲刺目标
                    else if((go<0 || projects[go].chose(sample.getExpertiseGain()))){
                        robot.takeFromCloud(sample);
                    }
                }
            }
        }

        @Override
        public void executeFinal() {
            if(robot.getSamples().size()>1){
                setCommand("GOTO " + MOLECULES);
            }
            else{
                limitResource=-1;
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
            for(int i=0;i<robot.getSamples().values().size();i++){

            }
        }

        @Override
        public void execute(Sample sample) {
            System.err.println(sample);
            if(sample.getCarriedBy()==0){
                //自己是ready了
                for(Type type: Type.types){
                    ////需要拿而且有
//                    System.err.println("还需要"+robot.getRequiredCount(type,sample.getSampleId())+"个"+type+",还有"+env.getAvailableCountByType(type)+"个");
                    if(robot.getRequiredCount(type,sample.getSampleId())>0 && env.getAvailableCountByType(type)>0){
//                        System.err.println("需要拿"+type);
                        robot.takeMolecule(type);
                        break;
                    }
                }
            }
        }

        @Override
        public void executeFinal() {
            //是否有sample ready了
            boolean hasReadySample = false;
            Set<Integer> readySamples = new HashSet<>();
            for(int id:robot.getSamples().keySet()){
                if(robot.getRequiredCount(id) ==0){
                    hasReadySample = true;
                    readySamples.add(id);
                }
            }
            //看readySamples是否可以拿
            for(Type type:Type.types){
                if(robot.getRequiredCount(type,readySamples)>0 && env.getAvailableCountByType(type)>0){
                    robot.takeMolecule(type);
                    break;
                }
            }
            System.err.println("readySamples" + readySamples);
            if(hasReadySample){
                setCommand("GOTO " + LABORATORY);
            }
            //type不够拿了,怎么办
            else{
                //如果敌人在放,就等等
                if(enemy.getLocation().equals(LABORATORY)){
                    setCommand("WAIT");
                }
                //敌人手上有至少5个，可能要去释放了
                else if(enemy.getLocation().equals(MOLECULES) && enemy.getEta()==0 && env.getAll()+ robot.getMoleculeCount()<=20){
                    setCommand("WAIT");
                }
                else{
                    if(command.isEmpty()){
                        limitResource = 1;
                        System.err.println("Set limitResource flag to 1");
                    }
                    setCommand("GOTO " + DIAGNOSIS);
                }
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
            System.err.println(sample);
            if(sample.getCarriedBy()==0){
                if(robot.getRequiredCount(sample.getSampleId()) ==0){
                    robot.produceOnLab(sample.getSampleId());
                }
            }
        }

        @Override
        public void executeFinal() {
            if(robot.getSamples().size()<=1){
                if((enemy.getLocation().equals(MOLECULES)
                        || enemy.getLocation().equals(LABORATORY)
                        || (enemy.getLocation().equals(SAMPLES) && enemy.getEta()>1))
                        &&validSampleInCloud()>= 3 - robot.getSamples().size()){
                    setCommand("GOTO " + DIAGNOSIS);
                }
                else {
                    setCommand("GOTO " + SAMPLES);
                }
            }
            else{
                setCommand("GOTO " + MOLECULES);
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
            if(robot.getRequiredCount(Type.A,this)>0){
                result = result && env.a>0;
            }
            if(robot.getRequiredCount(Type.B,this)>0){
                result = result && env.b>0;
            }
            if(robot.getRequiredCount(Type.C,this)>0){
                result = result && env.c>0;
            }
            if(robot.getRequiredCount(Type.D,this)>0){
                result = result && env.d>0;
            }
            if(robot.getRequiredCount(Type.E,this)>0){
                result = result && env.e>0;
            }
            if(!result){
                return enemy.getLocation().equals(LABORATORY) || (enemy.getLocation().equals(SAMPLES)&&sampleInEnemyhand.size()>=5);
            }
            return result;
        }

        @Override
        public int compareTo(Sample o) {
            return - this.getHealth().compareTo(o.getHealth());
        }
    }

    private enum State {
        diagnosed,undiagnosed,ready;
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

        int getExpertise(){
            return expertiseA+expertiseB+expertiseC+expertiseD+expertiseE;
        }

        boolean hasLevel3(){
            for(Sample sample:samples.values()){
                if(sample.getRank() == 3){
                    return true;
                }
            }
            return false;
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
                samples.remove(sampleId);

                for(Project project:projects){
                    project.reduce(sample.getExpertiseGain());
                }
            }
            setCommand("CONNECT " + sampleId);
        }

        void putToCloud(int sampleId){
            if(command.isEmpty()){
                samples.remove(sampleId);
            }
            setCommand("CONNECT " + sampleId);
        }

        void takeFromCloud(Sample sample){
            if(command.isEmpty()){
                samples.remove(sample.getSampleId());
            }
            setCommand("CONNECT " + sample.getSampleId());
        }

        void diagnosSample(Sample sample){
            if(command.isEmpty()) {
                samples.put(sample.getSampleId(),sample);
            }
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

    private String location;
    private int expertiseA;
    private int expertiseB;
    private int expertiseC;
    private int expertiseD;
    private int expertiseE;
    private int eta;
    private int score;


    public static int getLevel() {
        if(robot.getExpertise()<4){
            return 1;
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
        if(go>=0){
            return 2;
        }
        if(robot.getExpertise()>10){
            return 3;
        }
        else if(robot.getExpertise()>8 && !robot.hasLevel3()){
            return 3;
        }
        return 2;
    }


    public int getEta() {
        return eta;
    }

    public void setEta(int eta) {
        this.eta = eta;
    }

    public String getLocation() {
        return location;
    }

    public int getExpertiseA() {
        return expertiseA;
    }

    public int getExpertiseB() {
        return expertiseB;
    }

    public int getExpertiseC() {
        return expertiseC;
    }

    public int getExpertiseD() {
        return expertiseD;
    }

    public int getExpertiseE() {
        return expertiseE;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setExpertiseA(int expertiseA) {
        this.expertiseA = expertiseA;
    }

    public void setExpertiseB(int expertiseB) {
        this.expertiseB = expertiseB;
    }

    public void setExpertiseC(int expertiseC) {
        this.expertiseC = expertiseC;
    }

    public void setExpertiseD(int expertiseD) {
        this.expertiseD = expertiseD;
    }

    public void setExpertiseE(int expertiseE) {
        this.expertiseE = expertiseE;
    }

    public int getTotalExpertise(){
        return expertiseA + expertiseB + expertiseC + expertiseD + expertiseE;
    }

    @Override
    public String toString(){
        return new StringBuilder("Player信息:")
                .append("A技能等级:" + expertiseA)
                .append(",B技能等级:" + expertiseB)
                .append(",C技能等级:" + expertiseC)
                .append(",D技能等级:" + expertiseD)
                .append(",E技能等级:" + expertiseE)
                .toString();
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
}