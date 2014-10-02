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
	a = 3;
	b = 5;
	ff(a, a);
	a = 4;
	b = 10;
	ff(a, b);
	return 0;
}