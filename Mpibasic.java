import mpi.MPI;


public class Mpibasic {

	public static void main(String args[]) {
		
		//Initialize and finalize
		MPI.Init(args);

		int root = 0;
		//Rank and Size
		int size = MPI.COMM_WORLD.Size();
		int rank = MPI.COMM_WORLD.Rank();

		if(rank == root) {
			System.out.println("Size of MPI Communicator : "+size);
		}
		System.out.println("This process has rank "+rank);


		MPI.Finalize();
	}
}
