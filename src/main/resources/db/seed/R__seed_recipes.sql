-- Repeatable seed for local/dev data.
-- Idempotent: skips recipes that already exist by name.
-- Not loaded by the prod profile (see application.yaml vs application-prod.yaml).

INSERT INTO recipes (name, description, difficulty, image, recipe_ingredients, prepare, created_at, updated_at)
SELECT
    'Arroz Branco na Panela',
    'Arroz soltinho feito na panela comum, acompanhamento clássico do dia a dia.',
    1,
    'https://images.unsplash.com/photo-1536304993881-ff6e9eefa2a6',
    '[
        {"ingredient": "Arroz branco", "description": "2 xícaras", "image": null},
        {"ingredient": "Água", "description": "4 xícaras", "image": null},
        {"ingredient": "Alho", "description": "2 dentes picados", "image": null},
        {"ingredient": "Óleo", "description": "2 colheres (sopa)", "image": null},
        {"ingredient": "Sal", "description": "a gosto", "image": null}
    ]'::jsonb,
    '[
        "Lave o arroz em água corrente até a água sair limpa.",
        "Aqueça o óleo na panela e doure o alho.",
        "Adicione o arroz e refogue por 1 minuto.",
        "Junte a água fervente e o sal.",
        "Cozinhe em fogo baixo com a panela tampada por 15 minutos ou até a água secar."
    ]'::jsonb,
    NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM recipes WHERE name = 'Arroz Branco na Panela');

INSERT INTO recipes (name, description, difficulty, image, recipe_ingredients, prepare, created_at, updated_at)
SELECT
    'Feijão Carioca na Pressão',
    'Feijão carioca encorpado feito na panela de pressão, pronto em menos de 40 minutos.',
    2,
    'https://images.unsplash.com/photo-1604908176997-125f25cc6f3d',
    '[
        {"ingredient": "Feijão carioca", "description": "500g, deixado de molho por 4h", "image": null},
        {"ingredient": "Bacon", "description": "100g em cubos", "image": null},
        {"ingredient": "Cebola", "description": "1 média picada", "image": null},
        {"ingredient": "Alho", "description": "4 dentes picados", "image": null},
        {"ingredient": "Folha de louro", "description": "2 folhas", "image": null},
        {"ingredient": "Sal", "description": "a gosto", "image": null},
        {"ingredient": "Água", "description": "2 litros", "image": null}
    ]'::jsonb,
    '[
        "Escorra o feijão e reserve.",
        "Frite o bacon na panela de pressão até dourar.",
        "Acrescente cebola e alho e refogue.",
        "Adicione o feijão, o louro e a água.",
        "Tampe e cozinhe por 25 minutos após pegar pressão.",
        "Desligue, espere a pressão sair, acerte o sal e cozinhe mais 5 minutos sem tampa para encorpar."
    ]'::jsonb,
    NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM recipes WHERE name = 'Feijão Carioca na Pressão');

INSERT INTO recipes (name, description, difficulty, image, recipe_ingredients, prepare, created_at, updated_at)
SELECT
    'Carne Assada ao Forno',
    'Coxão mole assado lentamente, suculento e com molho encorpado.',
    3,
    'https://images.unsplash.com/photo-1558030006-450675393462',
    '[
        {"ingredient": "Coxão mole", "description": "1,5kg em peça", "image": null},
        {"ingredient": "Alho", "description": "6 dentes amassados", "image": null},
        {"ingredient": "Vinho tinto seco", "description": "200ml", "image": null},
        {"ingredient": "Cebola", "description": "2 grandes em rodelas", "image": null},
        {"ingredient": "Cenoura", "description": "2 em rodelas", "image": null},
        {"ingredient": "Alecrim", "description": "2 ramos", "image": null},
        {"ingredient": "Sal e pimenta", "description": "a gosto", "image": null},
        {"ingredient": "Azeite", "description": "3 colheres (sopa)", "image": null}
    ]'::jsonb,
    '[
        "Tempere a carne com alho, sal, pimenta e vinho e deixe marinar por 2h.",
        "Aqueça o azeite e sele a peça em fogo alto por todos os lados.",
        "Transfira para assadeira, adicione cebola, cenoura, alecrim e a marinada.",
        "Cubra com papel alumínio e asse a 180°C por 1h30.",
        "Retire o papel e asse por mais 20 minutos para dourar.",
        "Descanse por 10 minutos antes de fatiar contra as fibras."
    ]'::jsonb,
    NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM recipes WHERE name = 'Carne Assada ao Forno');

INSERT INTO recipes (name, description, difficulty, image, recipe_ingredients, prepare, created_at, updated_at)
SELECT
    'Lasanha à Bolonhesa',
    'Lasanha em camadas com molho bolonhesa, bechamel e muito queijo.',
    4,
    'https://images.unsplash.com/photo-1619895092538-128341789043',
    '[
        {"ingredient": "Massa de lasanha", "description": "500g", "image": null},
        {"ingredient": "Carne moída", "description": "700g", "image": null},
        {"ingredient": "Molho de tomate", "description": "800g", "image": null},
        {"ingredient": "Cebola", "description": "1 grande picada", "image": null},
        {"ingredient": "Alho", "description": "4 dentes picados", "image": null},
        {"ingredient": "Manteiga", "description": "60g", "image": null},
        {"ingredient": "Farinha de trigo", "description": "60g", "image": null},
        {"ingredient": "Leite", "description": "750ml", "image": null},
        {"ingredient": "Noz-moscada", "description": "a gosto", "image": null},
        {"ingredient": "Queijo mussarela", "description": "400g fatiado", "image": null},
        {"ingredient": "Parmesão ralado", "description": "100g", "image": null}
    ]'::jsonb,
    '[
        "Refogue cebola e alho, adicione a carne moída e doure.",
        "Junte o molho de tomate, tempere e cozinhe por 20 minutos.",
        "Para o bechamel: derreta a manteiga, junte a farinha, adicione o leite aos poucos e cozinhe até engrossar. Tempere com sal e noz-moscada.",
        "Monte em camadas: bolonhesa, massa, bechamel, mussarela. Repita até acabar.",
        "Finalize com parmesão por cima.",
        "Asse a 200°C por 35-40 minutos até borbulhar e dourar."
    ]'::jsonb,
    NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM recipes WHERE name = 'Lasanha à Bolonhesa');

INSERT INTO recipes (name, description, difficulty, image, recipe_ingredients, prepare, created_at, updated_at)
SELECT
    'Feijoada Completa',
    'Feijoada tradicional com carnes variadas, servida com arroz, couve, farofa e laranja.',
    5,
    'https://images.unsplash.com/photo-1625944525533-473f1b3d9684',
    '[
        {"ingredient": "Feijão preto", "description": "1kg, de molho por 12h", "image": null},
        {"ingredient": "Carne seca", "description": "500g dessalgada", "image": null},
        {"ingredient": "Costelinha salgada", "description": "500g dessalgada", "image": null},
        {"ingredient": "Linguiça calabresa", "description": "300g em rodelas", "image": null},
        {"ingredient": "Linguiça paio", "description": "300g em rodelas", "image": null},
        {"ingredient": "Bacon", "description": "200g em cubos", "image": null},
        {"ingredient": "Cebola", "description": "2 grandes picadas", "image": null},
        {"ingredient": "Alho", "description": "1 cabeça picada", "image": null},
        {"ingredient": "Folha de louro", "description": "3 folhas", "image": null},
        {"ingredient": "Laranja", "description": "1 inteira lavada", "image": null},
        {"ingredient": "Sal e pimenta", "description": "a gosto", "image": null}
    ]'::jsonb,
    '[
        "Dessalgue as carnes salgadas trocando a água várias vezes por 12h.",
        "Ferva as carnes separadamente por 15 minutos e escorra.",
        "Cozinhe o feijão com as carnes, louro e a laranja inteira em panela grande por 1h30-2h.",
        "Em outra panela, frite o bacon, adicione cebola e alho e refogue.",
        "Acrescente as linguiças e doure.",
        "Incorpore o refogado ao feijão, retire a laranja, acerte sal e pimenta.",
        "Cozinhe mais 20 minutos em fogo baixo para encorpar.",
        "Sirva com arroz branco, couve refogada, farofa e laranja em gomos."
    ]'::jsonb,
    NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM recipes WHERE name = 'Feijoada Completa');

-- Bump engagement stats so seeded recipes appear on the hot list.
-- Trigger already created hot_recipes rows with 0/0 on insert.
UPDATE hot_recipes hr
SET likes = seed.likes, views = seed.views
FROM (
    VALUES
        ('Arroz Branco na Panela',       320, 2400),
        ('Feijão Carioca na Pressão',    510, 3800),
        ('Carne Assada ao Forno',        780, 5200),
        ('Lasanha à Bolonhesa',         1240, 9100),
        ('Feijoada Completa',           2100, 15800)
) AS seed(name, likes, views)
JOIN recipes r ON r.name = seed.name
WHERE hr.recipe_id = r.id;
