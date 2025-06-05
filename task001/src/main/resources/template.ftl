Здравствуйте, ${body.fullname}!

Информируем вас о новой вакансии на должность ${body.position}.
Наименование: ${body.name}
Описание: ${(body.description)!"не указано"}
Уровень зарплаты: ${body.sallary}
Требуемый опыт работы: ${(body.requiredExperience)!"без опыта"}

С уважением,
${.now?string["dd.MM.yyyy"]}