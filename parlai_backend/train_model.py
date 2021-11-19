from parlai.scripts.train_model import TrainModel

TrainModel.main(
  # similar to before
  task='spr_teacher',
  model='transformer/generator',
  model_file='transfer_base/model',

  # initialize with a pretrained model
  init_model='zoo:blender/blender_90M/model',

  # arguments we get from the pretrained model.
  # Unfortunately, these must be looked up separately for each model.
  n_heads=16, n_layers=8, n_positions=512, text_truncate=512,
  label_truncate=128, ffn_size=2048, embedding_size=512,
  activation='gelu', variant='xlm',
  dict_lower=True, dict_tokenizer='bpe',
  dict_file='zoo:blender/blender_90M/model.dict',
  learn_positional_embeddings=True,

  # some training arguments, specific to this fine-tuning
  # use a small learning rate with ADAM optimizer
  lr=1e-5, optimizer='adam',
  warmup_updates=100,
  # early stopping on perplexity
  validation_metric='ppl',
  # train at most 10 minutes, and validate every 0.25 epochs
  max_train_time=600, validation_every_n_epochs=0.25,

  # depend on your gpu. If you have a V100, this is good
  batchsize=12, fp16=True, fp16_impl='mem_efficient',

  # speeds up validation
  skip_generation=True,

  # helps us cram more examples into our gpu at a time
  dynamic_batching='full',
)
