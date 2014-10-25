/*
main

f1
main
*/
//preprocessor
#include <stdio.h>
//structDeclaration
//structDefinition
//typedefDeclaration
//externDeclaration
//functionDeclaration
//globalDeclaration!!
//functions
void f1(int a, int b){
printf("%d%d\n", a, b);
}
int main(){
int n1;
int n2;
//---int s1;
//---int s2;
n1 = 0;
n2 = 0;
f1(n1, n2);
if(n1 >= 0) {
n2 = 3;
n1 = 7;
}
else 
 {
n2 = 6;
}

f1(n1, n2);
return 0;
}
