
# CVQS Back-End

CVQS Back-End mikroservis mimarisi ile geliştirilmiş araç hata kayıt uygulamasıdır.





## VERİ MODELLEMELERİ

ARAÇ - HATA - ARAÇ HATA


![ARAÇ - HATA - ARAÇ HATA](https://i.imgur.com/htWEPzb.png)

KULLANICI - ROL


![Kullanıcı](https://i.imgur.com/qb60P6a.png)

MİKROSERVİS MİMARİSİ


![Kullanıcı](https://i.imgur.com/xS7xikA.png)
## API Kullanımı

Request istekleri Ve Örnekleri Postman Koleksiyonunda paylaşılmıştır.

https://www.postman.com/material-candidate-82552888/workspace/my-workspace/collection/21648995-29c11bc8-7379-49b4-a056-f0647827a649?action=share&creator=21648995
## LOG

- AOP ile "annotion" lar sayesinde fonksiyonların girişlerinin ve çıkışlarının logları tutulmaktadır. 

 ![LOG](https://i.imgur.com/P4uA3GV.png)

 
 Örnek Çıktı :

 ![LOGCIKTI](https://i.imgur.com/vmBGiBO.png)




  
## MİKROSERVİS MİMARİSİ

Proje oluşturulurken 5 Konteynır ayağa kalkmaktadır bunlar :

- postgresql (Veri Tabanı) 
- auth (authentication ve authorization)
- management (kullanıcı ve rol tanımlamaları)
- cvqs (Araç - Hata - Araç Hata işlemleri)
- eurekaserver (konteynırların birbiri ile iletişimi)
  
## RESİM İŞLEME

- Resimler kullanıcıdan Hata kayıt edilirken veya güncellenirken alınır.

- Resimler kullanıcıdan base64 formatında string olarak alınmaktadır. Alınan resim byte dizisine dönüştürülür ve veri tabanında kaydedilir. Kullanıcı resmi geri istediğinde ise bu işlemlerin tersi yapılarak kullanıcıya resim iletilir.

- Araç Hata Kayıt yaparken kullanıcıdan hatanın olduğu kısımların resim üzerindeki kordinatları alınır. Alınan kordinatlar kaydedilir (Resim üzerinde yapılan işlem kaydedilmez). Kullanıcı hataların işaretlenmiş olduğu resimi istediğinde anlık olarak resimdeki konumlar işaretlenir ve kullanıcıya resim döndürülür. 

![RESİM İŞARETLEME](https://i.imgur.com/eIepi2B.png)
  
## JWT

Giriş yapan veya kayıt olan kullanıcılara jwt verilir. Bu token 3 kısım içerir :

- Kullanıcı Adı
- Oluşturulma Tarihi
- Bitiş Tarihi

Kullanıcı Servislere JWT ile istek yolladığında JWT in süresinin kontrolü yapılır. Eğer süresi hala geçerli ise veri tabanından kullanıcının rolleri getirilir ve bu sayede rol bazlı erişim yapılır. 
  ## ROL BAZLI ERİŞİM

 Öncelikle kullanıcının erişmek istediği adresin rol gereklilikleri belirlenir.
Eğer kullanıcının erişmek istediği adres herhangi bir rol yetkisi istiyor ise Feign client üzerinden auth konteynırına tokenden kullanıcı getirme isteği gönderilir. Getirilen kullanıcı bilgilerinin yetkisi kontrol edilip reddedilir veya kabul edilir.

Örnek rol yetkisi gereksinimleri :

![RESİM İŞARETLEME](https://i.imgur.com/Rcf34W1.png)

## SIRALAMA, FİLTRELEME VE SAYFALAMA

Listeleme işlemleri yapılırken kullanıcıdan kaçıncı sayfayı istediği, her sayfanın kaç değer içereceği, sıralama türü (DESC,ASC), ve Aranmak istenen anahtar kelimeler alınır.

Örnek Sorgu :

GET http://localhost:8080/api/v1/listing/getvehicles?modelNo=&vehicleCode=&page=0&pageSize=4&sortType=DESC

RESPONSE
 ```json
[
    {
        "id": 4,
        "modelNo": "i10",
        "vehicleCode": "i10-z23sz22-y",
        "vehicleDefectDtos": []
    },
    {
        "id": 3,
        "modelNo": "i20",
        "vehicleCode": "i20-z23sd2-y",
        "vehicleDefectDtos": []
    },
    {
        "id": 2,
        "modelNo": "A6",
        "vehicleCode": "A6-XTR523XZ",
        "vehicleDefectDtos": []
    },
    {
        "id": 1,
        "modelNo": "A4",
        "vehicleCode": "Z23S-1PK334",
        "vehicleDefectDtos": [
            {
                "id": 1,
                "defect": {
                    "id": 1,
                    "imageDto": {
                        "id": 1,
                        "name": "Araç kapı çizik",
                        "data": "",
                        "type": "jpg"
                    },
                    "name": "Araç kapı çizik"
                },
                "defectLocations": [
                    {
                        "id": 1,
                        "coordX": "50",
                        "coordY": "50",
                        "deleted": false
                    },
                    {
                        "id": 2,
                        "coordX": "450",
                        "coordY": "300",
                        "deleted": false
                    },
                    {
                        "id": 3,
                        "coordX": "230",
                        "coordY": "210",
                        "deleted": false
                    }
                ]
            }
        ]
    }
]
```
  
## DOCKER-COMPOSE VE DOCKERFILE

DOCKERFILE :

![DOCKERFILE](https://i.imgur.com/iZ3dXvz.png)

DOCKER-COMPOSE :

- Öncelikle veritabanı konteynırı ayağa kaldırılır. Bu konteynırın dinleyeceği port veritabanı ismi, kullanıcı ismi ve kullanıcı şifresi belirtilir. Konteynır silinip tekrar kullanılmaya başlandığında verileri kaybolmaması için veriler ‘‘db-data’’ isimli volümde tutulur.

![DOCKERFILE](https://i.imgur.com/7wi7TP2.png)
    
