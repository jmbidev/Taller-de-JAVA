package cycles.services;

import cycles.utils.CycleInformationBuilder;
import cycles.utils.GeneralCycles;
import cycles.utils.ParticularCycles;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServicesForControllers {
    public static final String NO_PACKAGE = "";
    private CyclesService cyclesService;
    private String references;
    private List<String> packagesID, packagesName;
    private GeneralCycles generalCycles;
    private ParticularCycles particularCycles;

    public ServicesForControllers(String path) {
        this.cyclesService = new CyclesService(path);
        this.cyclesService.runSAX();
        this.packagesID = new ArrayList<>();
        this.packagesName = new ArrayList<>();
        this.buildReferences();
        this.resetCycleInformationBuilders();
    }

    public void saveAsTextFile(String path, int limit, boolean isExactLimit, boolean isWithID, int maxNumberOfCyclesToShow, String package1, String package2){
        File f = new File(path+"/Cycles_TPFinal_Brisuela&Fluxá.txt");

        try{

            FileWriter w = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(w);
            PrintWriter wr = new PrintWriter(bw);

            this.getCycleInformationBuilder(package1, package2).saveInfoInTextFile(isWithID, isExactLimit, limit, maxNumberOfCyclesToShow, wr, this.references);

            wr.close();
            bw.close();

        } catch (IOException e){
            System.out.println("Error to write the file "+f);
        }
    }
    public void resetCycleInformationBuilders(){
        this.generalCycles = new GeneralCycles(this.cyclesService);
        this.particularCycles = new ParticularCycles(this.cyclesService);
    }
    public int getNumberOfPackages() {
        return this.cyclesService.getNumberOfPackages();
    }
    public int getNumberOfDependencies() {
        return this.cyclesService.getNumberOfDependencies();
    }
    public int getNumberOfCycles(boolean isParticularCase) {
        if (isParticularCase)   return this.particularCycles.getNumberOfCycles();
        return this.generalCycles.getNumberOfCycles();
    }
    public long getTarjanTime() {
        return this.cyclesService.getTarjanTime();
    }
    public boolean existPackage(String packageName) {
        return this.packagesName.contains(packageName);
    }
    public List<String> getPackagesOptions(boolean isWithID) {
        if (isWithID)   return this.packagesID;
        return this.packagesName;
    }
    public List<String> getPackagesName() {
        return this.packagesName;
    }
    public String getReferences() {
        return this.references;
    }
    public String getInformationToShow(String package1, String package2, boolean isWithID){
        return this.getCycleInformationBuilder(package1, package2).getInfoToFile();
    }
    public String getCyclesToShow(int limit, boolean isExactLimit, boolean isWithID, int maxNumberOfCyclesToShow, String package1, String package2) {
        if (isWithID && !package1.equals(ServicesForControllers.NO_PACKAGE) && !package2.equals(ServicesForControllers.NO_PACKAGE)){
            package1 = this.packagesName.get(Integer.valueOf(package1));
            package2 = this.packagesName.get(Integer.valueOf(package2));
        }
        this.cyclesService.runTarjan(limit, isExactLimit, package1, package2);
        return this.getCycleInformationBuilder(package1, package2).getInformationToShow(isWithID, maxNumberOfCyclesToShow);
    }

    // AUXILIARY METHODS
    private CycleInformationBuilder getCycleInformationBuilder(String package1, String package2){
        if (package1.equals(NO_PACKAGE) && package2.equals(NO_PACKAGE))
            return this.generalCycles;

        this.particularCycles.setPackage1(package1);
        this.particularCycles.setPackage2(package2);
        return this.particularCycles;
    }
    private void buildReferences() {
        StringBuilder references = new StringBuilder();
        int i = this.cyclesService.getNumberOfPackages();

        for (Long id : this.cyclesService.getPackages().keySet()) {
            references.append("   ");
            i--;
            String name = this.cyclesService.getPackages().get(id);
            this.packagesID.add(String.valueOf(id));
            this.packagesName.add(name);
            references.append(id);
            references.append(" -> ");
            references.append(name);

            if (i != 0)
                references.append("\n");
        }

        this.references = references.toString();
    }
}
