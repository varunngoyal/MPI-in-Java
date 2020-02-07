import mpi.MPI;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


public class mpi_file {

	public static void main(String args[]) {
		
		//Initialize and finalize
		MPI.Init(args);

		File file;
		Scanner sc=null;

		int root = 0;
		int data_size = 0;
		int chunk_size = 0;
		int sendbuf[];
		//Rank and Size
		int size = MPI.COMM_WORLD.Size();
		int rank = MPI.COMM_WORLD.Rank();

		//getting the size of data array
		try{
			file = new File(args[3]);
			sc = new Scanner(file);
			data_size = sc.nextInt();

		} catch(FileNotFoundException e) {
			System.out.println("Error: File not found!");
			System.exit(0);	
		}

		sendbuf = new int[data_size];
		chunk_size = data_size/size;

		if(rank == root) {
			System.out.println("Size of MPI Communicator : "+size);
		}
		System.out.println("Process with rank "+rank+" created!");

		// Initializing the send buffer



		if(rank == root) {

			//sendbuf[0] = 10;
			//sendbuf[1] = 20;
			//sendbuf[2] = 30;
			//sendbuf[3] = 40;

			//********************************************************************
			try {							
	
				int ctr = 0;

				while(sc.hasNextInt() && ctr < data_size) {
				
					sendbuf[ctr] = sc.nextInt();
					System.out.println("sendbuf["+ctr+"]: "+sendbuf[ctr]);
					ctr++;
				}

			} catch(Exception e) {
				System.out.println("Error occured in file operation! "+e);
				System.exit(0);
			}


			//********************************************************************			

			System.out.println("Data to be scattered by process "+rank+": ");
			for(int i=0;i<data_size;i++)
			{

				System.out.print(sendbuf[i]+" ");
			}
			System.out.println();	
		}
		int recvbuf[] = new int[chunk_size];

		//scatter
		MPI.COMM_WORLD.Scatter(sendbuf, 0, chunk_size, MPI.INT,
							   recvbuf, 0, chunk_size, MPI.INT,
							   root);

		//double the data
		//System.out.print ("Process "+rank+" has data: ");
		
		for(int i=0;i<chunk_size;i++) {
			System.out.println("Process "+rank+" doubling ["+i+"]: "+recvbuf[i]);
			recvbuf[i] = recvbuf[i]*2;
		}
		System.out.println();
		//gather
		MPI.COMM_WORLD.Gather(recvbuf, 0, chunk_size, MPI.INT,
							   sendbuf, 0, chunk_size, MPI.INT,
							   root);

		//display the doubled data
		if(rank == root) {

			//let's say chunk_size = 2 and size = 4
			//then if data size of 10 is given 
			//then 2 value remains
			//so it will cover  index 8 and 9
			for(int i = chunk_size*size; i < data_size; i++) {
	
				sendbuf[i] = sendbuf[i]*2;
			}
			System.out.println("The root process "+rank+"has data: ");
			for(int i=0;i<data_size;i++)
				System.out.print(sendbuf[i]+" ");
		}

		MPI.Finalize();	
	}
}
