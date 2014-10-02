#include <stdio.h>

int ff(int p1, int p2){
	printf("\t%d\n", p1+p2);
	return p1+p2;
}
void ffd(int *p){
	*p = 10;
}


int main(){
	int a;
	int b;
	a = 0;
	b = 0;
	if(a == 0){
		a = 5;
		b = 5;
	}
	else{
		a = 3;
		b = 3;
	}
	ff(a, a);
	//a = 4;
	//b = 10;
	//ff(a, b);
	return 0;
}