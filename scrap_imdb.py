'''
A script to scrape top 250 movies from IMDB
'''

from bs4 import BeautifulSoup
import requests

#Raspando os dados do IMDB
URLS = 'http://www.IMDB.com/chart/top'
PAGES = requests.get(URLS)
SOUP = BeautifulSoup(PAGES.text, 'html.parser')

MOVIES = SOUP.select('td.titleColumn')

IMAGES=[]
for k in SOUP.select('td.posterColumn img'):
    IMAGES.append(k.attrs.get('src'))

SCORE = []
for b in SOUP.select('td.posterColumn span[name=ir]'):
    SCORE.append(b.attrs.get('data-value'))


#Formata os dados
IMDB = []
for k in range(0, len(MOVIES)):
    titulo = MOVIES[k].get_text().split()
    IMDB.append(str(titulo[0])+' '+' '.join(titulo[1:-1])+' '+str(titulo[-1])+' '+ IMAGES[k])

#Gera arquivo com os dados
i = 0
dados = open('dados.txt', 'w')
for k in IMDB:
    dados.write(k +' - '+'%.1f' %float(SCORE[i]))
    dados.write('\n')
    i += 1
dados.close()
