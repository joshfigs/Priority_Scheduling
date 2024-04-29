import java.util.*;

public class Process {

    // Method to safely read a double, ensuring no invalid characters are accepted
    private static double readDouble(Scanner scanner, String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            scanner.next(); // Clear the invalid input
            System.out.println("Invalid input. Please enter a numeric value.");
            System.out.print(prompt);
        }
        return scanner.nextDouble();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int numProcesses = scanner.nextInt();

        List<ActionItem> allProcesses = new ArrayList<>();
        PriorityQueue<ActionItem> readyQueue = new PriorityQueue<>(
                Comparator.comparingInt(ActionItem::getPrio).thenComparingDouble(ActionItem::getArrival));

        for (int i = 0; i < numProcesses; i++) {
            System.out.println("\nProcess " + (i + 1));
            double arrivalTime = readDouble(scanner, "Arrival Time: ");
            double burstTime = readDouble(scanner, "Burst Time: ");
            int priority = (int) readDouble(scanner, "Priority: ");

            allProcesses.add(new ActionItem(priority, arrivalTime, burstTime));
        }
        scanner.close();

        allProcesses.sort(Comparator.comparingDouble(ActionItem::getArrival));

        double currentTime = 0;
        double totalTurnAroundTime = 0;
        double totalWaitingTime = 0;

        // Print the table header
        System.out.println("+---------+-------------+------------+----------+----------------+------------------+---------------+");
        System.out.println("| Process | Arrival Time| Burst Time | Priority | Completion Time| Turn Around Time | Waiting Time  |");
        System.out.println("+---------+-------------+------------+----------+----------------+------------------+---------------+");

        int processNumber = 1; // Track process number

        while (!allProcesses.isEmpty() || !readyQueue.isEmpty()) {
            // Load new processes into the ready queue as they arrive
            while (!allProcesses.isEmpty() && allProcesses.get(0).getArrival() <= currentTime) {
                readyQueue.offer(allProcesses.get(0));
                allProcesses.remove(0); // Remove the added process
            }

            if (!readyQueue.isEmpty()) {
                ActionItem currentProcess = readyQueue.poll();

                double nextEventTime = !allProcesses.isEmpty() ? allProcesses.get(0).getArrival() : Double.MAX_VALUE;

                double timeSlice = Math.min(currentProcess.getRemainingBurst(), nextEventTime - currentTime);
                currentProcess.setRemainingBurst(currentProcess.getRemainingBurst() - timeSlice);
                currentTime += timeSlice;

                if (currentProcess.getRemainingBurst() <= 0) {
                    double completionTime = currentTime;
                    double turnAroundTime = completionTime - currentProcess.getArrival();
                    double waitingTime = turnAroundTime - currentProcess.getBurst();
                    totalTurnAroundTime += turnAroundTime;
                    totalWaitingTime += waitingTime;

                    System.out.printf("| P%d      | %12.1f | %10.1f | %8d | %14.1f | %16.1f | %13.1f |\n",
                            processNumber++, currentProcess.getArrival(), currentProcess.getBurst(),
                            currentProcess.getPrio(), completionTime, turnAroundTime, waitingTime);
                } else {
                    readyQueue.offer(currentProcess);
                }
            } else {
                // Advance time to the arrival of the next process
                currentTime = allProcesses.get(0).getArrival();
            }
        }

        // Print the table footer with totals and averages
        System.out.println("+---------+-------------+------------+----------+----------------+------------------+---------------+");
        System.out.printf("| Total   |             |            |          |                | %16.1f | %13.1f |\n",
                totalTurnAroundTime, totalWaitingTime);
        System.out.println("+---------+-------------+------------+----------+----------------+------------------+---------------+");
        System.out.printf("| Average |             |            |          |                | %16.1f | %13.1f |\n",
                totalTurnAroundTime / numProcesses, totalWaitingTime / numProcesses);
        System.out.println("+---------+-------------+------------+----------+----------------+------------------+---------------+");
    }
}
