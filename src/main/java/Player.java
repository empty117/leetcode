import java.util.*;

/**
 * Bring data on patient samples from the diagnosis machine to the laboratory with enough molecules to produce medicine!
 **/
class Player {

    private static final String SAMPLES = "SAMPLES", DIAGNOSIS = "DIAGNOSIS", MOLECULES = "MOLECULES", LABORATORY = "LABORATORY";

    private static Robot robot = new Robot();
    private static Player enemy = new Player();


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
    }

    private static int leftStep(){
        if(round<=120){
            return 3;
        }
        else if(round<=170){
            return 1;
        }
        return -999;
    }

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int projectCount = in.nextInt();
        Project[] projects = new Project[3];
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
            round++;

            for(int i=0;i<projectCount;i++){
//                System.err.println("第"+i+"个project,还差"+projects[i].getTotal()+"个");
                if(go == -1){
                    if(projects[i].getTotal()<=leftStep()){
//                        System.err.println("我决定冲刺第"+i+"个project,还差"+projects[i].getTotal()+"个");
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
            int sampleCount = in.nextInt();
            boolean flag = false;
            String location = robot.location;
            int eta = robot.eta;
            System.err.println("location="+location+",sampleCount="+sampleCount + ",player.getEta="+eta);
            Machine machine = getMachine(location);
            machine.before();

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
            machine.executeFinal();

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
        boolean done =false;
        @Override
        public void before() {
            if(!robot.location.equals(SAMPLES) || robot.eta>0){
                done = true;
                System.out.println("GOTO " + SAMPLES);
            }
        }

        @Override
        public void execute(Sample sample) {
            if(done){
                return;
            }
            System.err.println(sample);
            //刷新库存
            if(sample.getCarriedBy()==0){
                if(robot.getSamples().get(sample.getSampleId())==null){
                    robot.getSamples().put(sample.getSampleId(),sample);
                }
                else{
                    return;
                }

            }
            if(robot.getSamples().size()<3){
                if(robot.getSamples().size() == 2){
                    //敌人还在拿就先走
                    if(enemy.getLocation().equals(SAMPLES) && enemy.getEta() == 0){
                        done = true;
                        System.out.println("GOTO " + DIAGNOSIS);
                    }
                }
                else{
                    done = true;
                    System.out.println("CONNECT " + getLevel());
                }
            }
            else{
                done = true;
                System.out.println("GOTO " + DIAGNOSIS);
            }
        }

        @Override
        public void executeFinal() {
            if(!done){
                System.out.println("CONNECT 1");
            }
        }
    }

    private static class DiggnosisMachine implements Machine{
        boolean done =false;
        @Override
        public void before() {
            if(robot.eta>0){
                done = true;
                System.out.println("GOTO " + DIAGNOSIS);
            }
        }

        @Override
        public void execute(Sample sample) {
            if(done){
                return;
            }
            System.err.println(sample);
            if(sample.carriedBy==0){
                //未分析的sample
                if(sample.getState() == State.undiagnosed){
                    done = true;
                    robot.diagnosSample(sample);
                }
                //已分析的
                else{
                    if(!sample.isValid()){
                        done = true;
                        robot.putToCloud(sample.getSampleId());
                    }
                }
            }
            else if(sample.carriedBy == -1){
                if(sample.isValid()){
                    done = true;
                    robot.takeFromCloud(sample);
                }
            }
        }

        @Override
        public void executeFinal() {
            if(!done){
                System.out.println("GOTO " + MOLECULES);
            }
        }
    }

    private static class MoleculeMachine implements Machine{
        boolean done = false;
        @Override
        public void before() {
            if(robot.eta>0){
                done = true;
                System.out.println("GOTO " + MOLECULES);
                return;
            }
            for(int i=0;i<robot.getSamples().values().size();i++){
                
            }
        }

        @Override
        public void execute(Sample sample) {
            if(done){
                return;
            }
            System.err.println(sample);
            if(sample.getCarriedBy()==0){
                //如果A不够
                if(sample.getCostAForRobot()>robot.moleculesA.size()){
                    done = true;
                    robot.takeMolecule(Type.A);
                }
                else if(sample.getCostBForRobot()>robot.moleculesB.size()){
                    done = true;
                    robot.takeMolecule(Type.B);
                }
                else if(sample.getCostCForRobot()>robot.moleculesC.size()){
                    done = true;
                    robot.takeMolecule(Type.C);
                }
                else if(sample.getCostDForRobot()>robot.moleculesD.size()){
                    done = true;
                    robot.takeMolecule(Type.D);
                }
                else if(sample.getCostEForRobot()>robot.moleculesE.size()){
                    done = true;
                    robot.takeMolecule(Type.E);
                }
                else{
                    robot.getSamples().get(sample.getSampleId()).setState(State.ready);
                }
            }
        }

        @Override
        public void executeFinal() {
            if(!done){
                boolean ready = false;
                for(Sample sample:robot.getSamples().values()){
                    if(sample.getState() == State.ready){
                        ready = true;
                        break;
                    }
                }
                if(ready){
                    System.out.println("GOTO " + LABORATORY);
                }
                else{
                    System.out.println("WAIT");
                }
            }
        }
    }

    private static class LabMachine implements Machine{
        boolean done =false;
        @Override
        public void before() {
            if(robot.eta>0){
                done = true;
                System.out.println("GOTO " + LABORATORY);
            }
        }

        @Override
        public void execute(Sample sample) {
            if(done){
                return;
            }
            System.err.println(sample);
            if(sample.getCarriedBy()==0){
                if(sample.getState()==State.ready){
                    done = true;
                    robot.produceOnLab(sample.getSampleId());
                }
            }
        }

        @Override
        public void executeFinal() {
            if(!done){
                if(robot.getSamples().size()<=1){
                    System.out.println("GOTO " + SAMPLES);
                }
                else{
                    System.out.println("GOTO " + MOLECULES);
                }
            }
        }
    }

    private static boolean isChosenType(Project project, String gain){
        if(project.getTotal()<=0){
            return true;
        }
        switch (gain){
            case "A": return project.a > 0;
            case "B": return project.b > 0;
            case "C": return project.c > 0;
            case "D": return project.d > 0;
            case "E": return project.e > 0;
            default:return true;
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

        Type(String value){
            this.value = value;
        }

        String value(){
            return value;
        }
    }

    private static class Robot {

        List<Type> moleculesA = new ArrayList<>();
        List<Type> moleculesB = new ArrayList<>();
        List<Type> moleculesC = new ArrayList<>();
        List<Type> moleculesD = new ArrayList<>();
        List<Type> moleculesE = new ArrayList<>();
        int rA=0,rB=0,rC=0,rD=0,rE=0;
        Map<Integer,Sample> samples = new HashMap<>(3);

        int expertiseA, expertiseB,expertiseC,expertiseD,expertiseE;
        String location;
        int eta,score;

        /*
        减库存，发命令
         */
        void produceOnLab(int sampleId){
            Sample sample =  samples.get(sampleId);
            int a = sample.getCostAForRobot();
            int b = sample.getCostBForRobot();
            int c = sample.getCostCForRobot();
            int d = sample.getCostDForRobot();
            int e = sample.getCostEForRobot();
            if(a>0){
                for(int i=0;i<a;i++){
                    moleculesA.remove(i);
                }
            }
            if(b>0){
                for(int i=0;i<b;i++){
                    moleculesB.remove(i);
                }
            }
            if(c>0){
                for(int i=0;i<c;i++){
                    moleculesC.remove(i);
                }
            }
            if(d>0){
                for(int i=0;i<d;i++){
                    moleculesD.remove(i);
                }
            }
            if(e>0){
                for(int i=0;i<e;i++){
                    moleculesE.remove(i);
                }
            }
            samples.remove(sampleId);
            System.out.println("CONNECT " + sampleId);
        }

        void putToCloud(int sampleId){
            samples.remove(sampleId);
            System.out.println("CONNECT " + sampleId);
        }

        void takeFromCloud(Sample sample){
            samples.putIfAbsent(sample.getSampleId(),sample);
            System.out.println("CONNECT " + sample.getSampleId());
        }

        void diagnosSample(Sample sample){
            samples.put(sample.getSampleId(),sample);
            System.out.println("CONNECT " + sample.getSampleId());
        }

        void takeMolecule(Type type){
            if(robot.getMoleculeCount()>=10){
                return;
            }
            switch (type){
                case A: moleculesA.add(type);rA++;break;
                case B: moleculesB.add(type);rB++;break;
                case C: moleculesC.add(type);rC++;break;
                case D: moleculesD.add(type);rD++;break;
                case E: moleculesE.add(type);rE++;break;
            }
            System.out.println("CONNECT " + type.value());
        }

        int getMoleculeCount(){
            return moleculesA.size()+moleculesB.size()+moleculesC.size()+moleculesD.size()+moleculesE.size();
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

    }

    private String location;
    private int expertiseA;
    private int expertiseB;
    private int expertiseC;
    private int expertiseD;
    private int expertiseE;
    private int eta;


    public static int getLevel() {
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

    static boolean isValid(Set<Sample> samples){
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
}