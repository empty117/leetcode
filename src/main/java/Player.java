import java.util.*;

/**
 * Bring data on patient samples from the diagnosis machine to the laboratory with enough molecules to produce medicine!
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

    private static final int THRESHOLD = 5;
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
        if(round<=120){
            return 3;
        }
        else if(round<=160){
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
                        System.err.println("我决定冲刺第"+i+"个project,还差"+projects[i].getTotal()+"个");
                        go = i;
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
                    if(enemy.getLocation().equals(SAMPLES) && enemy.getEta() == 0){
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
                    if(limitResource==0 && sample.getRank()<getLevel()){
                        if(command.isEmpty()){
                            System.err.println("Set limitResource flag to -1");
                            limitResource=-1;
                        }
                        System.err.println("Set limitResource flag to -1");
                        limitResource=-1;

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
            if(robot.getSamples().size()>0){
                setCommand("GOTO " + MOLECULES);
            }
            else{
                limitResource=-1;
                setCommand("GOTO " + SAMPLES);
            }
        }
    }

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
                System.err.println(robot.getSamples().get(sample.getSampleId()).costA);
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
            //是否可以去LAB
            boolean ready = false;
            for(int id:robot.getSamples().keySet()){
                if(robot.getRequiredCount(id) ==0){
                    ready = true;
                    break;
                }

            }
            if(ready){
                setCommand("GOTO " + LABORATORY);
            }
            //type不够拿了,怎么办
            else{
                //如果敌人在放,就等等
                if(enemy.getLocation().equals(LABORATORY)){
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
                setCommand("GOTO " + SAMPLES);
            }
            else{
                setCommand("GOTO " + MOLECULES);
            }
        }
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

        boolean isValid(){
            boolean staticValid = getCostAForRobot()<=5 && getCostBForRobot()<=5
                    && getCostCForRobot()<=5 && getCostDForRobot()<=5
                    && getCostEForRobot()<=5 && getTotolCostForRobot()<=10;
            return staticValid;
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

        public int getRequiredCount(Type type, int sampleId){
            int count = 0;
            switch (type){
                case A:
                    count += robot.getSamples().get(sampleId).getCostAForRobot();
                    count -= countA;
                    break;
                case B:
                    count += robot.getSamples().get(sampleId).getCostBForRobot();
                    count -= countB;
                    break;
                case C:
                    count += robot.getSamples().get(sampleId).getCostCForRobot();
                    count -= countC;
                    break;
                case D:
                    count += robot.getSamples().get(sampleId).getCostDForRobot();
                    count -= countD;
                    break;
                case E:
                    count += robot.getSamples().get(sampleId).getCostEForRobot();
                    count -= countE;
                    break;
            }
            return Math.max(0,count);
        }

        public int getRequiredCount(int sampleId){
            int count = 0;
            for(Type type:Type.types){
                count += getRequiredCount(type,sampleId);
            }
            return count;
        }

        public int getRequiredCount(Type type){
            int count = 0;
            switch (type){
                case A:
                    for(Sample sample: robot.getSamples().values()){
                        count +=sample.getCostAForRobot();
                    }
                    count -= countA;
                    return count;
                case B:
                    for(Sample sample: robot.getSamples().values()){
                        count +=sample.getCostBForRobot();
                    }
                    count -= countB;
                    return count;
                case C:
                    for(Sample sample: robot.getSamples().values()){
                        count +=sample.getCostCForRobot();
                    }
                    count -= countC;
                    return count;
                case D:
                    for(Sample sample: robot.getSamples().values()){
                        count +=sample.getCostDForRobot();
                    }
                    count -= countD;
                    return count;
                case E:
                    for(Sample sample: robot.getSamples().values()){
                        count +=sample.getCostEForRobot();
                    }
                    count -= countE;
                    return count;
                default:
                    return count;
            }
        }

    }

    private String location;
    private int expertiseA;
    private int expertiseB;
    private int expertiseC;
    private int expertiseD;
    private int expertiseE;
    private int eta;


    public static int getLevel() {
        if(robot.getExpertise()<4){
            return 1;
        }
//        else if(robot.getExpertise()>8){
//            return 3;
//        }
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

    static boolean isValid(Sample ... samples){
        int a=0,b=0,c=0,d=0,e=0;
        for(Sample sample: samples){
            a+=sample.getCostAForRobot();
            b+=sample.getCostBForRobot();
            c+=sample.getCostCForRobot();
            d+=sample.getCostDForRobot();
            e+=sample.getCostEForRobot();
        }
        boolean staticValid = a<=5 && b<=5
                && c<=5 && d<=5
                && e<=5 && a+b+c+d+e<=10;
        return staticValid;
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
    }
}