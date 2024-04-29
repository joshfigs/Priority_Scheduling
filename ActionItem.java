class ActionItem {
   private int prio;
   private double arrival;
   private double burst;
   private double remainingBurst; // Track the remaining burst time for preemption

   // Constructor
   public ActionItem(int prio, double arrival, double burst) {
       this.prio = prio;
       this.arrival = arrival;
       this.burst = burst;
       this.remainingBurst = burst;
   }

   // Getters and setters
   public int getPrio() {
       return prio;
   }

   public double getArrival() {
       return arrival;
   }

   public double getBurst() {
       return burst;
   }

   public double getRemainingBurst() {
       return remainingBurst;
   }

   public void setRemainingBurst(double remainingBurst) {
       this.remainingBurst = remainingBurst;
   }

   public String toString() {
      return "ActionItem{arrival=" + arrival + ", burst=" + burst + ", prio=" + prio + ", remainingBurst=" + remainingBurst + "}";
  }
  
}