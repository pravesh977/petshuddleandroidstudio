package android.portfolio.petshuddle.Entity;

public class Pet {

    private int petId;
    private String petName;
    private String species;
    private String sex;
    private String breed;
    private int age;
    private String petDescription;
    private String userId;

    public Pet(int petId, String petName, String species, String sex, String breed, int age, String petDescription, String userId) {
        this.petId = petId;
        this.petName = petName;
        this.species = species;
        this.sex = sex;
        this.breed = breed;
        this.age = age;
        this.petDescription = petDescription;
        this.userId = userId;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPetDescription() {
        return petDescription;
    }

    public void setPetDescription(String petDescription) {
        this.petDescription = petDescription;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

