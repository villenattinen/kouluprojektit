import string

finnish_char_freq = ['A','I','T','N','E','S','L','O','K','U','Ä','M','V','R','J','H','Y','P','D','Ö','G','B','F','C','W','Å','Q']
characters = []
char_freq = []
char2char = []
encrypted_text = 'MNTMGMZIFC MCN MNTMGMZIFCWWNBZZB ATIXNMBTT MNTPGH BCCMCFZZPTH, WZGMMCAZXBTWWNBZZPTH QC TUTLPTH LWWSÖNMSANBMS. TWN LUMTTHFTMGHC FGNPCCH BCHGC, TMMS MNTMGMZIFCH MCIXGNMZB GIVCHNBCCMNGBBC GH GWTACBBC GWTFCH MNWCHMTTH BSNWLMMSANHTH. BNWWGNH XCNXXN GH HNNH XZNH ÖNMSS. MNTMGQSIQTBMTWANS XSLMTMSSH MCIXGNMTMZWWC MCFCWWC QC MZIFCWWNBTBMN. MNTMGMZIFCH MSIXTNAÖNS IGGWTQC GH BNMTH LWWSÖNMSS GIVCHNBCCMNGH XLXLS MGNANC, BCACWWC FCIANBMCTH MNTPGH QC MTXHGWGVNC GACNBZZPTH MZIFCC QC XSLMMSANBMS. MSAS FNTBMN GH BCWCMMZ MNTMGMZIFCXZIBBNWWT LXBNCCXXGBMGBCWCCQCWWC.'

def count_freq():
    for char in encrypted_text:
        if char not in string.punctuation + ' ':
            characters.append(char)
    characters.sort()

    for char in characters:
        char_count = characters.count(char)
        if [char, char_count] not in char_freq:
            char_freq.append([char, char_count])
    char_freq.sort(key=lambda x:x[1], reverse=True)
    
    for i in range(len(char_freq)):
        char2char.append([char_freq[i][0], finnish_char_freq[i]])

def write_file(outputFile):
    string2write = ''
    for char in encrypted_text:
        if char not in string.punctuation + ' ':
            index = [y[0] for y in char2char].index(char)
            char = char2char[index][1]
        string2write+=char
    print('\n' + encrypted_text + '\n')
    print(string2write + '\n')
    with open(outputFile, 'w', encoding='utf-8') as output_file:
        output_file.write(string2write)

def interface():
    letters = sorted(finnish_char_freq)
    i = 0
    while i < len(char2char):
        print('\nAseta uusi kirjain ehdotetun tilalle, paina vain enter pitääksesi kirjaimen ehdotettuna tai kirjoita \'skip\' hypätäksesi loput kirjaimet yli\n')
        print('Käytettävissä olevat kirjaimet: ' + str(letters) + '\n')
        new_char = input('Anna ehdotus kirjaimelle ' + char2char[i][0] + ' (suositeltu ' + char2char[i][1] + '): ').upper()
        if new_char.strip() == 'SKIP':
            print('\nSkipataan manuaalinen kirjainten valinta, käytetään tähän asti valittuja/oletuksia\n')
            break
        elif new_char == '':
            print('\nPidetään kirjain oletuksena\n')
            print(char2char[i][0] + ' = ' + char2char[i][1])
            letters.remove(char2char[i][1])
            i+=1
            continue
        elif new_char not in letters:
            print("\nEi kelvollinen kirjain")
        else:
            char2char[i] = [char2char[i][0], new_char]
            letters.remove(new_char)
            print(char2char[i][0] + ' = ' + new_char)
            i+=1
        
if __name__ == "__main__":
    count_freq()
    interface()
    write_file('harjoitus3_ehdotus.txt')
