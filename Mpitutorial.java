import mpi.MPI;


public class Mpitutorial {

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

		// Initializing the send buffer

		int sendbuf[] = new int[size];

		if(rank == root) {

			sendbuf[0] = 10;
			sendbuf[1] = 20;
			sendbuf[2] = 30;
			sendbuf[3] = 40;

			System.out.println("Data to be scattered by process "+rank+": ");
			for(int i=0;i<size;i++)
			{

				System.out.print(sendbuf[i]+" ");
			}	
		}
		int recvbuf[] = new int[1];
	
		//scatter
		MPI.COMM_WORLD.Scatter(sendbuf, 0, 1, MPI.INT,
							   recvbuf, 0, 1, MPI.INT,
							   root);

		//double the data
		System.out.println("Process "+rank+" has data: "+recvbuf[0]);		
		recvbuf[0] = recvbuf[0]*2;
		
		//gather
		MPI.COMM_WORLD.Gather(recvbuf, 0, 1, MPI.INT,
							   sendbuf, 0, 1, MPI.INT,
							   root);

		//display the doubled data
		if(rank == root) {
			System.out.println("The root process "+rank+"has data: ");
			for(int i=0;i<size;i++)
				System.out.print(sendbuf[i]+" ");
		}

		MPI.Finalize();
	}
}
