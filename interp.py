import re
from pprint import pprint
code="""clear X;
incr X;
incr X;
clear Y;
incr Y;
incr Y;
incr Y;
clear Z;
while X not 0 do;
   clear W;
   while Y not 0 do;
      incr Z;
      incr W;
      decr Y;
   end;
   while W not 0 do;
      incr Y;
      decr W;
   end;
   decr X;
end;
copy X to Y"""
code2='''incr N;
incr N;
incr N;
incr N;
incr N;
incr N;
incr N;
incr N;
incr N;
copy N to input;
clear F;
incr F;
decr N;

while N not 0 do;
  copy F to G;
  while G not 0 do;
    copy N to H;
    while H not 0 do;
      incr F;
      decr H;
    end;
    decr G;
  end;
  decr N;
end;'''
def compile(code):
    tokens=[]
    for line in [x for x in code.split(';') if not re.match('^\\s*$',x)]:
        tokens.append(token(*[x for x in re.split('\\W',line) if not re.match('^\\s*$',x)]))
    dtokens=[]

    for i,t in enumerate(tokens):
        if 'do' in t.arg:
            dtokens.append((i,t))
        if 'end' == t.oporator:
            di,dt=dtokens.pop()
            dt.arg.append(i)
            t.arg.append(di)

    for i,t in enumerate(tokens):
        print('{0} : {1}'.format(i,str(t)))
    a=raw_input()
    cenv={'__pc__':0}
    p=str(cenv)
    while cenv['__pc__']<len(tokens):
        tokens[cenv['__pc__']].exe(cenv)
        # if p != str(cenv):
    print(cenv)
            #  p=str(cenv)
            #  a=raw_input()

    return

class token(object):
    def __init__(self,*arg,**kargs):
        self.oporator=arg[0]
        self.arg=list(arg[1:])
        self.kargs=kargs
        self.oporations={'incr':self.incr,'clear':self.clear,'decr':self.decr,'while':self.my_while,'end':self.end,'copy':self.cpy}
    def exe(self,cenv):
        if self.oporator in self.oporations.keys():

            self.oporations[self.oporator](cenv)
        else:
            pprint('unknown token {0} at comand {1}'.format(t,i))
    def cpy(self,cenv):
        term1=self.arg[0]
        term2=self.arg[-1]
        if term1 in cenv.keys():
            cenv[term2]=cenv[term1]
        cenv['__pc__']+=1
    def my_while(self,cenv):
        valuation=False
        term1=self.arg[0]
        oporator=self.arg[1]
        term2=self.arg[2]
        if re.match('^\\d+$',term1):
            term1=int(term1)
        else:
            term1=cenv[term1]

        if re.match('^\\d+$',term2):
            term2=int(term2)
        else:
            term2=cenv[term2]
            # pprint(term2)
            #pprint(term2)
        valuation|= oporator=='not' and term1 != term2
        #pprint(self.arg)
        #valuation|= oporator=='is' and term1 == term2
        if valuation:
            cenv['__pc__']+=1
        else:
            cenv['__pc__']=self.arg[-1]+1
    def end(self,cenv):
        cenv['__pc__']=self.arg[-1]
    def clear(self,cenv):
        for a in self.arg:
            cenv[a]=0
        cenv['__pc__']+=1
    def decr(self,cenv):
        for identifyer in self.arg:
            if identifyer in cenv.keys():
                cenv[identifyer]-=1
            else:
                cenv[identifyer]=-1
        cenv['__pc__']+=1
    def incr(self,cenv):
        for identifyer in self.arg:
            if identifyer in cenv.keys():
                cenv[identifyer]+=1
            else:
                cenv[identifyer]=1
        cenv['__pc__']+=1
    def __str__(self):
        return str({x:y for x,y in self.__dict__.items() if x!='oporations'})
    def __repr__(self):
        return str({x:y for x,y in self.__dict__.items() if x!='oporations'})
if __name__=='__main__':
    compile(code)
    #compile(code2)
